/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.gradle.plugin

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue

class KtlintPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        val ktlint: Configuration by target.configurations.creating

        val versionCatalog =
            try {
                target
                    .rootProject
                    .extensions
                    .getByType<VersionCatalogsExtension>()
                    .named("tonyLibs")
            } catch (e: InvalidUserDataException) {
                target.logger.warn(e.message)
                return
            }

        target.dependencies {
            ktlint(versionCatalog.findLibrary("ktlint").get())
            // ktlint(project(":custom-ktlint-ruleset")) // in case of custom ruleset
        }
        val outputDir = "${target.layout.buildDirectory.get()}/reports/ktlint/"
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

        target.tasks.register("ktlintFormat", JavaExec::class.java) {
            inputs.files(inputFiles)
            outputs.dir(outputDir)

            description = "Fix Kotlin code style deviations."
            group = "verification"
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            jvmArgs = mutableListOf("--add-opens", "java.base/java.lang=ALL-UNNAMED")
            args("-F", "src/main/**/*.kt")
        }
        target.tasks.named("compileKotlin") {
            dependsOn("ktlintCheck")
        }
    }
}
