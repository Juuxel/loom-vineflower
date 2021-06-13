plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("build-logic")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "io.github.juuxel"
version = "1.0.0+quiltflower.1.4.0"

if (file("private.gradle").exists()) {
    apply(from = "private.gradle")
}

val shade by configurations.creating

configurations {
    compileOnly {
        extendsFrom(shade)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net")
    }

    maven {
        name = "Quilt"
        url = uri("https://maven.quiltmc.org/repository/release")
    }
}

dependencies {
    implementation(gradleApi())

    implementation("net.fabricmc:fabric-loom:0.7.32")
    implementation("net.fabricmc:fabric-fernflower:1.4.1")
    implementation("net.fabricmc:tiny-mappings-parser:0.3.0+build.17")
    implementation("org.ow2.asm:asm:9.1")

    shade("org.quiltmc:quiltflower:1.4.0") {
        attributes {
            attribute(juuxel.loomquiltflower.plugin.RemapState.REMAP_STATE_ATTRIBUTE, juuxel.loomquiltflower.plugin.RemapState.REMAPPED)
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(11)
    }

    jar {
        archiveClassifier.set("slim")
        from(file("LICENSE"), file("LICENSE.quiltflower.txt"))
    }

    shadowJar {
        archiveClassifier.set("")
        configurations = listOf(shade)
    }

    assemble {
        dependsOn(shadowJar)
    }
}

gradlePlugin {
    plugins {
        create("loom-quiltflower") {
            id = "io.github.juuxel.loom-quiltflower"
            implementationClass = "juuxel.loomquiltflower.LoomQuiltflowerPlugin"
        }
    }
}

artifacts.apiElements(tasks.shadowJar)
artifacts.runtimeElements(tasks.shadowJar)

val env = System.getenv()
if ("MAVEN_URL" in env) {
    publishing {
        repositories {
            maven {
                url = uri(env.getValue("MAVEN_URL"))
                credentials {
                    username = env.getValue("MAVEN_USERNAME")
                    password = env.getValue("MAVEN_PASSWORD")
                }
            }
        }
    }
}
