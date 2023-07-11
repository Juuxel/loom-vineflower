rootProject.name = "build-logic"

includeBuild("../shared") {
    dependencySubstitution {
        substitute(module("io.github.juuxel:vineflower-for-loom-core")).using(project(":"))
    }
}
