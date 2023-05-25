# loom-quiltflower

![Build status](https://img.shields.io/github/actions/workflow/status/Juuxel/LoomQuiltflower/build.yml?style=flat-square&branch=master)
![Maven metadata](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/io/github/juuxel/loom-quiltflower/maven-metadata.xml.svg?label=latest%20plugin%20version&style=flat-square&color=49bfe0)
![Quiltflower version](https://img.shields.io/maven-metadata/v/https/maven.quiltmc.org/repository/release/org/quiltmc/quiltflower/maven-metadata.xml.svg?label=latest%20quiltflower&style=flat-square&color=fc9505)

A Loom addon that adds [Quiltflower](https://github.com/QuiltMC/Quiltflower) as a Loom decompiler
for non-Quilt distributions of Loom (upstream, Architectury etc.).

- [Version compatibility](#version-compatibility)
- [Getting started](#getting-started)
  - [Fabric projects](#fabric-projects)
  - [Architectury projects](#architectury-projects)
- [Configuration](#configuration)
  - [Quiltflower version](#quiltflower-version)
  - [Quiltflower sources](#quiltflower-sources)
  - [Decompilation preferences](#decompilation-preferences)

## Version compatibility

| Loom variant        | Plugin ID               | Supported versions                                  |
|---------------------|-------------------------|-----------------------------------------------------|
| Fabric Loom         | `fabric-loom`           | 0.8, 0.9, 0.11, 0.12, 1.0, 1.1, 1.2                           |
| Architectury Loom   | `dev.architectury.loom` | 0.7.2, 0.7.4, 0.10.0¹, 0.11.0, 0.12.0, 1.0, 1.1, 1.2 |
| Quilt Loom          | `org.quiltmc.loom`      | 0.12, 1.0, 1.1, 1.2                                           |
| `gg.essential.loom` | `gg.essential.loom`     | *None*²                                             |
| Babric Loom         | `babric-loom`           | 0.12²                                               |
| Ornithe Loom        | `ornithe-loom`          | 1.0²                                                |

¹ From build 0.10.0.206 onwards  
² Completely untested

Older versions might be compatible, but using them is unsupported. Bugs caused by outdated Loom versions will not be fixed.

For the latest Fabric Loom 0.10 versions (0.10.28+),
use [loom-quiltflower-mini](https://github.com/Juuxel/loom-quiltflower-mini) instead.

## Getting started

> Note: versions before 1.7.1 required adding Cotton's maven repository,
> but LQF 1.7.1+ is available on the Gradle Plugin Portal
> like most other plugins.

### Fabric projects

1. Add loom-quiltflower to your plugins:
```diff
  plugins {
      id 'fabric-loom' version '1.0-SNAPSHOT'
+     id 'io.github.juuxel.loom-quiltflower' version '1.8.0'
      id 'maven-publish'
  }
```

2. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

### Architectury projects

1. Add loom-quiltflower to your `plugins` block:
```diff
  plugins {
      id "architectury-plugin" version "3.4-SNAPSHOT"
      id "dev.architectury.loom" version "1.0-SNAPSHOT" apply false
+     id 'io.github.juuxel.loom-quiltflower' version '1.8.0' apply false
  }
```

2. Apply loom-quiltflower to subprojects:

```diff
  subprojects {
      apply plugin: "dev.architectury.loom"
+     apply plugin: "io.github.juuxel.loom-quiltflower"
```

> Note: this can also be done in the subprojects' `plugins` blocks.

3. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

## Configuration

### Quiltflower version

You can configure the used version of Quiltflower with the `quiltflower` extension (called `loomQuiltflower` before 1.2.0):

```groovy
quiltflower {
    // This is the default; 1.4.0 and above should work
    quiltflowerVersion.set("1.9.0")

    // If you're using Groovy DSL, you can also specify the version like this:
    quiltflowerVersion = '1.9.0'
}
```

### Quiltflower sources

> Added in LQF 1.2.0.

In addition to specifying a version, you can also use a completely different *Quiltflower source*.
They are configured with the `quiltflower.source` property.

```kotlin
quiltflower {
    // Downloads the wanted QF version from the QuiltMC Maven repo.
    // This is the default behaviour.
    fromQuiltMaven()

    // Downloads the wanted QF version from the project repositories.
    fromProjectRepositories()

    // Resolves QF using a Gradle dependency.
    // The parameter can be any dependency notation supported by Gradle.
    fromDependency("a:b:1.2.3")

    // Downloads or copies the wanted QF version from a URL.
    fromUrl("https://address.to/the/quiltflower.jar")

    // Uses a local QF file
    fromFile("my-quiltflower.jar")
}
```

### Decompilation preferences

> Added in LQF 1.2.0.

You can also change the preferences used to decompile the game.
For changing the properties, you can either use their 3-letter names or, with some properties, a preset method.

Preset methods as of 1.4.0:

| Method | ID | Description | Default |
|--------|----|-------------|----------|
| `inlineSimpleLambdas` | `isl` | Collapse single-line lambdas | 1 |
| `useJadVarnaming` | `jvn` | Use JAD-style local variable naming from ForgeFlower | 0 |
| `patternMatching` | `pam` | Pattern matching support[^1] | 1 |
| `experimentalTryLoopFix` | `tlf` | Fix for interactions between `try` and loops[^1] | 1 |

[^1]: Used to be experimental, but considered stable as of Quiltflower 1.9

#### Groovy DSL
```groovy
quiltflower {
    preferences {
        // fake options: don't try at home
        abc = 1
        ghi = 'thing'

        inlineSimpleLambdas 0
    }
}
```

#### Kotlin DSL
```kotlin
quiltflower {
    preferences(
        // fake options: don't try at home
        "abc" to 1,
        "ghi" to "thing",
    )

    preferences.inlineSimpleLambdas(0)
}
```

#### gradle.properties

> Added in LQF 1.3.0.

Preferences can also be declared in gradle.properties files using their 3-letter names, prefixed
with `loom-quiltflower.preference.`.

```properties
# Fake option here too :^)
loom-quiltflower.preference.abc = 1
```

You can use the global gradle.properties file in the Gradle user home directory to set your
wanted properties, like the indentation string, for each project that uses LQF.

#### Decompiler options

> Added in LQF 1.6.0. Only works on Fabric Loom 0.11+!

You can also configure the options with Loom's new
decompiler options API:

*Groovy DSL*

```groovy
loom {
    decompilers {
        quiltflower {
            options += [
                // optionName: "value"
                abc: "a"
            ]
        }
    }
}
```

*Kotlin DSL*

```kotlin
loom {
    decompilers {
        getByName("quiltflower") {
            options.put("<option name>", "<value>")
        }
    }
}
```
