package com.tony.gradle.plugin

import com.palantir.gradle.docker.DockerExtension
import com.tony.gradle.plugin.Build.Companion.getImageNameFromProperty
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate

class DockerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply {
            plugin("com.palantir.docker")
            plugin("org.springframework.boot")
        }

        val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
        val dockerRegistry: String by project
        val dockerUserName: String by project
        val dockerPassword: String by project
        val dockerNameSpace: String by project
        val imageNameFromProperty = project.getImageNameFromProperty()
        val nameSpace: String = dockerNameSpace.ifBlank { dockerRegistry }

        project.tasks.register("dockerLogin", Exec::class.java) {
            group = "docker"
            executable = "docker"
            args(
                listOf(
                    "login",
                    "-u",
                    dockerUserName,
                    "-p",
                    dockerPassword,
                    dockerRegistry
                )
            )
        }

        lateinit var taskNameList: List<String>

        project.extensions.getByType<DockerExtension>().apply {
            //final name:my.registry.com/username/my-app:version
            name = "$dockerRegistry/$nameSpace/${imageNameFromProperty}"
            val today = yyyyMMdd.format(Date())
            tag("today", "$name:$today")
            tag("latest", "$name:latest")
            setDockerfile(File("Dockerfile"))
            // implicit task dep
            val outputs = project.tasks.getByPath("bootJar").outputs
            copySpec
                .from(outputs)
                .into("")
            buildArgs(
                mapOf(
                    "JAR_FILE" to outputs.files.singleFile.name
                )
            )
            taskNameList =
                namedTags
                    .keys
                    .plus(project.version.toString())
                    .map { "dockerPush${it.capitalized()}" }
        }

        project.afterEvaluate {
            taskNameList.forEach {
                project.tasks.getByName(it) {
                    dependsOn("dockerLogin")
                }
            }
        }
    }
}
