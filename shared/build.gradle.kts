import java.util.Properties

plugins {
    `java-library`
}

repositories {
    mavenCentral()

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net")
    }
}

dependencies {
    compileOnly(gradleApi())

    val versions = Properties()
    file("../gradle.properties").inputStream().use { versions.load(it) }

    api("org.ow2.asm:asm:${versions["asm-version"]}")
    api("org.ow2.asm:asm-commons:${versions["asm-version"]}")
    implementation("net.fabricmc:tiny-remapper:${versions["tiny-remapper-version"]}")
}
