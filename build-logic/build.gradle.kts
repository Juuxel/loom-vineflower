import java.util.Properties

plugins {
    `groovy-gradle-plugin`
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
}

dependencies {
    val versions = Properties()
    file("../gradle.properties").inputStream().use { versions.load(it) }

    implementation("org.ow2.asm:asm:${versions["asm-version"]}")
    implementation("org.ow2.asm:asm-commons:${versions["asm-version"]}")
    implementation("net.fabricmc:tiny-remapper:${versions["tiny-remapper-version"]}")
    implementation("com.google.guava:guava:${versions["guava-version"]}")
    implementation("io.github.juuxel:loom-quiltflower-core")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(11)
    }
}
