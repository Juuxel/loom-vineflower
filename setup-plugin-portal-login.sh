#!/usr/bin/env bash

echo "project.ext.set('gradle.publish.key', '$GRADLE_PUBLISH_KEY')" > private.gradle
echo "project.ext.set('gradle.publish.secret', '$GRADLE_PUBLISH_SECRET')" >> private.gradle
