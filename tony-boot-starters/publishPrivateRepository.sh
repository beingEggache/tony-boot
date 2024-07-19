#!/usr/bin/env bash
./gradlew build-script:publishJarPublicationToPrivateGradleRepository

./gradlew ktlintFormat
./gradlew clean
./gradlew publishPomPublicationToPrivateRepository
./gradlew publishCatalogPublicationToPrivateRepository
./gradlew publishJarPublicationToPrivateRepository --build-cache
