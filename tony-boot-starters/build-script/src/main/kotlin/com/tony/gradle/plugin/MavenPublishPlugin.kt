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

package com.tony.gradle.plugin

import java.net.URI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register

class MavenPublishPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply {
            plugin("org.gradle.maven-publish")
        }
        val isPom = project.extra.has("pom")
        val isCatalog = project.extra.has("catalog")

        if (!isPom && !isCatalog) {
            project.extensions.getByType<JavaPluginExtension>().apply {
                withSourcesJar()
                withJavadocJar()
            }
        }

        val releasesRepoUrl: String by project
        val snapshotsRepoUrl: String by project
        val nexusUsername: String by project
        val nexusPassword: String by project

        project.extensions.getByType<PublishingExtension>().apply {
            repositories {
                maven {
                    name = "private"
                    url =
                        URI(if (project.version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                    isAllowInsecureProtocol = true
                    credentials {
                        username = nexusUsername
                        password = nexusPassword
                    }
                }
            }
            publications {
                if (isPom) {
                    register("pom", MavenPublication::class) {
                        from(project.components["javaPlatform"])
                    }

                } else if (isCatalog) {
                    register("catalog", MavenPublication::class) {
                        from(project.components["versionCatalog"])
                    }
                } else {
                    register("jar", MavenPublication::class) {
                        from(project.components["kotlin"])
                    }
                    register("jarAndSrc", MavenPublication::class) {
                        from(project.components["kotlin"])
                        artifact(project.tasks["sourcesJar"])
                    }
                }
            }
        }
    }
}
