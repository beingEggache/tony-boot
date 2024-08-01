#!/usr/bin/env bash

./gradlew versionCatalogUpdate --warning-mode=none --exclude-task=versionCatalogFormat --interactive
