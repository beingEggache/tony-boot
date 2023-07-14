#!/usr/bin/env bash
./gradlew build-script:publishJarAndSrcPublicationToMavenLocal

./gradlew ktlintFormat
./gradlew publishPomPublicationToMavenLocal --build-cache
./gradlew publishJarAndSrcPublicationToMavenLocal --build-cache
