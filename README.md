# loom-quiltflower

![Maven metadata](https://img.shields.io/maven-metadata/v/https/server.bbkr.space/artifactory/libs-release/io/github/juuxel/loom-quiltflower/maven-metadata.xml.svg?style=flat-square&color=49bfe0)

A Loom addon that adds [Quiltflower](https://github.com/QuiltMC/Quiltflower) as a Loom decompiler
for non-Quilt distributions of Loom (upstream, Architectury etc.).

- [Version compatibility](#version-compatibility)
- [Getting started](#getting-started)
  - [Fabric projects](#fabric-projects)
  - [Architectury projects](#architectury-projects)
- [Configuration](#configuration)

## Version compatibility

Loom variant      | Supported versions
------------------|-------------------
Fabric Loom       | 0.7 - 0.9
Architectury Loom | 0.7.2 - 0.8.0

## Getting started

### Fabric projects

1. Add the Cotton maven repository to settings.gradle:
```diff
  pluginManagement {
      repositories {
          maven {
              name = 'Fabric'
              url = 'https://maven.fabricmc.net/'
          }
+         maven {
+             name = 'Cotton'
+             url = 'https://server.bbkr.space/artifactory/libs-release/'
+         }          
          gradlePluginPortal()
      }
  }
```

2. Add loom-quiltflower to your plugins:
```diff
  plugins {
      id 'fabric-loom' version '0.8-SNAPSHOT'
+     id 'io.github.juuxel.loom-quiltflower' version '1.1.0'
      id 'maven-publish'
  }
```

3. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

### Architectury projects

1. Add the Cotton maven repository to settings.gradle:
```diff
  pluginManagement {
      repositories {
          maven { url "https://maven.fabricmc.net/" }
          maven { url "https://maven.architectury.dev/" }
          maven { url "https://maven.minecraftforge.net/" }
+         maven {
+             name = 'Cotton'
+             url = 'https://server.bbkr.space/artifactory/libs-release/'
+         }
          gradlePluginPortal()
      }
  }
```

2. Add loom-quiltflower to your `plugins` block:
```diff
  plugins {
      id "architectury-plugin" version "3.1-SNAPSHOT"
      id "dev.architectury.loom" version "0.7.2-SNAPSHOT" apply false
+     id 'io.github.juuxel.loom-quiltflower' version '1.1.0' apply false
  }
```

3. Apply loom-quiltflower to subprojects:

```diff
  subprojects {
      apply plugin: "dev.architectury.loom"
+     apply plugin: "io.github.juuxel.loom-quiltflower"
```

> Note: this can also be done in the subprojects' `plugins` blocks.

4. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

## Configuration

You can configure the used version of Quiltflower with the `loomQuiltflower` extension:

```groovy
loomQuiltflower {
    quiltflowerVersion.set("1.4.0")
}
```
