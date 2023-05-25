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

rootProject.name = "loom-quiltflower"
includeBuild("build-logic")

includeBuild("shared") {
    dependencySubstitution {
        substitute(module("io.github.juuxel:loom-quiltflower-core")).using(project(":"))
    }
}
