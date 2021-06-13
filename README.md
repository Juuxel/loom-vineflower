# loom-quiltflower

![Maven metadata](https://img.shields.io/maven-metadata/v/https/server.bbkr.space/artifactory/libs-release/io/github/juuxel/loom-quiltflower/maven-metadata.xml.svg?style=flat-square&color=49bfe0)

A Loom addon that adds [Quiltflower](https://github.com/QuiltMC/Quiltflower) as a Loom decompiler
for non-Quilt distributions of Loom (upstream, Architectury etc.).

## Usage

See further down for Architectury instructions.

### Usage with Fabric projects

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
+     id 'io.github.juuxel.loom-quiltflower' version '1.0.1+quiltflower.1.4.0'
      id 'maven-publish'
  }
```

3. Instead of `genSources`, you can now use `genSourcesWithQuiltflower`.

### Usage with Architectury projects

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
+     id 'io.github.juuxel.loom-quiltflower' version '1.0.1+quiltflower.1.4.0' apply false
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
