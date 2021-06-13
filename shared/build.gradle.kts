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
    api("org.ow2.asm:asm:9.1")
    api("org.ow2.asm:asm-commons:9.1")
    implementation("net.fabricmc:tiny-remapper:0.4.1")
}
