#!/usr/bin/env bash
./gradlew build-script:clean
./gradlew build-script:publishJarAndSrcPublicationToMavenLocal

./gradlew clean
./gradlew publishPomPublicationToMavenLocal
./gradlew publishJarAndSrcPublicationToMavenLocal
