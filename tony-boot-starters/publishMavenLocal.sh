#!/usr/bin/env bash
./gradlew build-script:publishJarAndSrcPublicationToMavenLocal

./gradlew ktlintFormat
./gradlew clean
./gradlew publishPomPublicationToMavenLocal
./gradlew publishCatalogPublicationToMavenLocal
./gradlew publishJarAndSrcPublicationToMavenLocal --build-cache
