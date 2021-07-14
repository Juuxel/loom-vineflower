plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("build-logic")
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "io.github.juuxel"
version = "1.1.2"

if (file("private.gradle").exists()) {
    apply(from = "private.gradle")
}

val shade by configurations.creating {
    isTransitive = false
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

    compileOnly("net.fabricmc:fabric-loom:${property("loom-version")}")
    implementation("net.fabricmc:fabric-fernflower:${property("fabric-fernflower-version")}")
    implementation("net.fabricmc:tiny-mappings-parser:${property("tiny-mappings-parser-version")}")
    implementation("net.fabricmc:tiny-remapper:${property("tiny-remapper-version")}")
    implementation("org.ow2.asm:asm:${property("asm-version")}")
    implementation("org.ow2.asm:asm-commons:${property("asm-version")}")

    // Only needed for providing the classes to compile against, it is downloaded at runtime
    compileOnly(loomQuiltflowerLogic.quiltflower())
    compileOnly("io.github.juuxel:loom-quiltflower-core")
    shade("io.github.juuxel:loom-quiltflower-core")
}

blossom {
    replaceToken("CURRENT_QUILTFLOWER_VERSION", property("quiltflower-version"))
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
            implementationClass = "juuxel.loomquiltflower.api.LoomQuiltflowerPlugin"
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
