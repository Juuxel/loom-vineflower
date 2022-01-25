plugins {
    `java-gradle-plugin`
    groovy
    `maven-publish`
    id("build-logic")
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "io.github.juuxel"
version = "1.6.0"

if (file("private.gradle").exists()) {
    apply(from = "private.gradle")
}

val shade by configurations.creating {
    isTransitive = false // don't include the unnecessary outside deps

    isCanBeConsumed = false
    isCanBeResolved = true
}

val loomRuntime by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations {
    compileClasspath {
        extendsFrom(shade)
    }

    runtimeClasspath {
        extendsFrom(shade)
        extendsFrom(loomRuntime)
    }
}

val arch by sourceSets.registering
val loom011 by sourceSets.registering
val secondarySourceSets = listOf(arch, loom011)

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withSourcesJar()
}

val (loomId, loomVersion) = (System.getenv("TEST_LOOM_VARIANT") ?: "fabric-loom:0.8-SNAPSHOT").split(":")

repositories {
    mavenCentral()

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net")
    }

    maven {
        name = "Arch"
        url = uri("https://maven.architectury.dev")
    }

    maven {
        name = "Forge"
        url = uri("https://maven.minecraftforge.net")
    }

    maven {
        name = "Quilt"
        url = uri("https://maven.quiltmc.org/repository/release")
    }

    mavenLocal()
}

dependencies {
    implementation(gradleApi())

    // Loom dependencies
    compileOnly("net.fabricmc:fabric-loom:${property("loom-version")}")
    shade("net.fabricmc:mapping-io:${property("mapping-io-version")}")
    shade("net.fabricmc:stitch:${property("stitch-version")}")
    shade("net.fabricmc:tiny-remapper:${property("tiny-remapper-version")}")
    compileOnly("org.ow2.asm:asm:${property("asm-version")}")
    compileOnly("org.ow2.asm:asm-commons:${property("asm-version")}")

    // Only needed for providing the classes to compile against, it is downloaded at runtime
    compileOnly(loomQuiltflowerLogic.quiltflower())
    shade("io.github.juuxel:loom-quiltflower-core") {
        isTransitive = false
    }

    // Actual dependencies that aren't shaded
    implementation("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.jetbrains:annotations:21.0.1")

    for (sourceSet in secondarySourceSets) {
        val config = "${sourceSet.name}CompileOnly"
        config(gradleApi())
        config(sourceSets.main.map { it.output })
    }
    "archCompileOnly"("dev.architectury:architectury-loom:0.10.0.206")
    "loom011CompileOnly"("net.fabricmc:fabric-loom:0.11.17")
    "loom011CompileOnly"(loomQuiltflowerLogic.quiltflower())

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.20.2")
    // This has to be a runtimeClasspath dep because gradle's test kit classpath stuff is really dumb.
    loomRuntime("$loomId:$loomId.gradle.plugin:$loomVersion")
    for (sourceSet in secondarySourceSets) {
        loomRuntime(sourceSet.map { it.output })
    }
}

blossom {
    replaceToken("CURRENT_QUILTFLOWER_VERSION", property("quiltflower-version"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(11)
    }

    named<JavaCompile>("compileArchJava") {
        options.release.set(16)
    }

    named<JavaCompile>("compileLoom011Java") {
        options.release.set(17)
    }

    jar {
        archiveClassifier.set("slim")
        from(file("LICENSE"), file("LICENSE.quiltflower.txt"))
        for (sourceSet in secondarySourceSets) {
            from(sourceSet.map { it.output })
        }
    }

    shadowJar {
        archiveClassifier.set("")
        configurations = listOf(shade)
        for (sourceSet in secondarySourceSets) {
            from(sourceSet.map { it.output })
        }

        relocate("net.fabricmc.mappingio", "juuxel.loomquiltflower.impl.relocated.mappingio")
        relocate("net.fabricmc.tinyremapper", "juuxel.loomquiltflower.impl.relocated.tinyremapper")
        relocate("net.fabricmc.stitch", "juuxel.loomquiltflower.impl.relocated.stitch")
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

        testLogging {
            showStackTraces = true
            showExceptions = true
            showCauses = true
            showStandardStreams = true
        }
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
