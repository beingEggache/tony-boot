package com.tony.buildscript

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue

class KtlintPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        val ktlint: Configuration by target.configurations.creating

        target.dependencies {
            ktlint("com.pinterest:ktlint:0.50.0")
            // ktlint(project(":custom-ktlint-ruleset")) // in case of custom ruleset
        }
        val outputDir = "${target.buildDir}/reports/ktlint/"
        val inputFiles: ConfigurableFileTree = target.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

        target.tasks.register("ktlintCheck", JavaExec::class.java) {
            inputs.files(inputFiles)
            outputs.dir(outputDir)

            description = "Check Kotlin code style."
            group = "verification"
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            args("src/main/**/*.kt")
        }

        target.tasks.register("ktlintFormat",JavaExec::class.java) {
            inputs.files(inputFiles)
            outputs.dir(outputDir)

            description = "Fix Kotlin code style deviations."
            group = "verification"
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            args("-F", "src/main/**/*.kt")
        }
        target.tasks.named("compileKotlin") {
            dependsOn("ktlintCheck")
        }
    }
}
