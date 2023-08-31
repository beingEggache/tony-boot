@file:Suppress("ConstPropertyName")

package com.tony.buildscript

object KaptDeps {

    object SpringBoot {
        const val configurationProcessor =
            "org.springframework.boot:spring-boot-configuration-processor:${VersionManagement.SPRING_BOOT_VERSION}"
        const val autoconfigureProcessor =
            "org.springframework.boot:spring-boot-autoconfigure-processor:${VersionManagement.SPRING_BOOT_VERSION}"
    }

    object Spring {
        const val contextIndexer = "org.springframework:spring-context-indexer:${VersionManagement.SPRING_VERSION}"
    }
}
