package com.tony.buildscript

object KaptDeps {

    object SpringBoot {
        const val configurationProcessor =
            "org.springframework.boot:spring-boot-configuration-processor:${VersionManagement.springBootVersion}"
        const val autoconfigureProcessor =
            "org.springframework.boot:spring-boot-autoconfigure-processor:${VersionManagement.springBootVersion}"
    }

    object Spring {
        const val contextIndexer = "org.springframework:spring-context-indexer:${VersionManagement.springVersion}"
    }
}
