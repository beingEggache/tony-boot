package com.tony.buildscript

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*
import java.net.URI

class MavenPublishPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply {
            plugin("org.gradle.maven-publish")
        }
        val isPom = project.extra.has("pom")

        if (!isPom) {
            project.configure<JavaPluginExtension> {
                withSourcesJar()
                withJavadocJar()
            }
        }

        val releasesRepoUrl: String by project
        val snapshotsRepoUrl: String by project
        val nexusUsername: String by project
        val nexusPassword: String by project

        project.configure<PublishingExtension> {
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
                if (!isPom) {
                    register("jar", MavenPublication::class) {
                        from(project.components["kotlin"])
                    }

                    register("jarAndSrc", MavenPublication::class) {
                        from(project.components["kotlin"])
                        artifact(project.tasks["sourcesJar"])
                    }
                } else {
                    register("pom", MavenPublication::class) {
                        from(project.components["javaPlatform"])
                    }
                }
            }
        }
    }
}
