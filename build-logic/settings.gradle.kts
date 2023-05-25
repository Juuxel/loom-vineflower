rootProject.name = "build-logic"

includeBuild("../shared") {
    dependencySubstitution {
        substitute(module("io.github.juuxel:loom-quiltflower-core")).with(project(":"))
    }
}
