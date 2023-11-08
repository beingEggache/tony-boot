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
