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

configurations {
    compileClasspath {
        extendsFrom(shade)
    }

    runtimeClasspath {
        extendsFrom(shade)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val testLoomVariant = System.getenv("TEST_LOOM_VARIANT") ?: "fabric0_9"
val loomEntry: Pair<String, String> = when (testLoomVariant) {
    "fabric0_8" -> "fabric-loom" to "0.8-SNAPSHOT"
    "fabric0_9" -> "fabric-loom" to "0.9-SNAPSHOT"
    "arch0_7_2" -> "dev.architectury.loom" to "0.7.2-SNAPSHOT"
    "arch0_7_3" -> "dev.architectury.loom" to "0.7.3-SNAPSHOT"
    "arch0_8_0" -> "dev.architectury.loom" to "0.8.0-SNAPSHOT"
    "arch0_9_0" -> "dev.architectury.loom" to "0.9.0-SNAPSHOT"
    else -> error("unknown loom variant: $testLoomVariant")
}
val (loomId, loomVersion) = loomEntry

repositories {
    mavenCentral()

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net")
    }

    if (loomId == "dev.architectury.loom") {
        maven {
            name = "Arch"
            url = uri("https://maven.architectury.dev")
        }

        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net")
        }
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
    shade("io.github.juuxel:loom-quiltflower-core")

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.20.2")
    // This has to be a runtimeOnly because gradle's test kit classpath stuff is really dumb.
    runtimeOnly("$loomId:$loomId.gradle.plugin:$loomVersion")
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

    test {
        useJUnitPlatform()
        systemProperties(
            "loomId" to loomId,
            "fabric.loom.test" to "surely",
        )
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
