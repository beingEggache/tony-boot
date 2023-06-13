#!/usr/bin/env bash
./gradlew build-script:clean
./gradlew build-script:publishJarAndSrcPublicationToMavenLocal

./gradlew clean
./gradlew ktlintFormat
./gradlew publishPomPublicationToMavenLocal --build-cache
./gradlew publishJarAndSrcPublicationToMavenLocal --build-cache
