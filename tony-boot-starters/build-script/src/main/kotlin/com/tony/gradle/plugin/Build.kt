package com.tony.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Build is
 * @author tangli
 * @date 2023/11/08 14:00
 * @since 1.0.0
 */
class Build : Plugin<Project> {
    override fun apply(target: Project) {
        println("Build")
    }
}
