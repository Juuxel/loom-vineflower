pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net")
        }
    }
}

rootProject.name = "loom-vineflower"
includeBuild("build-logic")

includeBuild("shared") {
    dependencySubstitution {
        substitute(module("io.github.juuxel:vineflower-for-loom-core")).using(project(":"))
    }
}
