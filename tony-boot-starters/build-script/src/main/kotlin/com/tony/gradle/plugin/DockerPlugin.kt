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
