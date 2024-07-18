#!/usr/bin/env bash
./gradlew build-script:publishJarPublicationToPrivateGradleRepository

./gradlew ktlintFormat
./gradlew clean
./gradlew publishPomPublicationToPrivateRepository --build-cache
./gradlew publishCatalogPublicationToPrivateRepository --build-cache
./gradlew publishJarPublicationToPrivateRepository --build-cache
