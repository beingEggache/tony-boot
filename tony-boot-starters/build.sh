#!/usr/bin/env bash
./gradlew build-script:publishJarAndSrcPublicationToMavenLocal

./gradlew ktlintFormat
./gradlew clean
./gradlew publishPomPublicationToMavenLocal --build-cache
./gradlew publishCatalogPublicationToMavenLocal --build-cache
./gradlew publishJarAndSrcPublicationToMavenLocal --build-cache
