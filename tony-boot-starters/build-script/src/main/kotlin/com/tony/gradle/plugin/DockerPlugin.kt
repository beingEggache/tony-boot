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
import com.tony.gradle.plugin.Build.Companion.propFromSysOrProject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.getByType

class DockerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply {
            plugin("com.palantir.docker")
            plugin("com.palantir.docker-compose")
            plugin("org.springframework.boot")
        }

        val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
        val dockerRegistry: String = project.propFromSysOrProject("dockerRegistry")
        val dockerUserName: String = project.propFromSysOrProject("dockerUserName")
        val dockerPassword: String = project.propFromSysOrProject("dockerPassword")
        val dockerNameSpace: String = project.propFromSysOrProject("dockerNameSpace")
        val projectName: String = project.propFromSysOrProject("projectName", project.name)
        val dockerImageFullName =
            listOf(
                dockerRegistry,
                dockerNameSpace,
                projectName
            ).filter {
                it.isNotBlank()
            }.joinToString("/")

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

        project.tasks.register("dockerSave", Exec::class.java) {
            group = "docker"
            executable = "docker"
            args(
                listOf(
                    "save",
                    "-o",
                    "${projectName}.tar",
                    dockerImageFullName,
                )
            )
        }

        val taskNameList: List<String>

        project.extensions.getByType<DockerExtension>().apply {
            //final name:my.registry.com/username/my-app:version
            name = dockerImageFullName
            val today = yyyyMMdd.format(Date())
            tag("today", "$name:$today")
            tag("latest", "$name:latest")
            setDockerfile(File(project.propFromSysOrProject("Dockerfile", "Dockerfile")))
            // implicit task dep
            val outputs =
                project
                    .tasks
                    .getByPath("bootJar")
                    .outputs
            copySpec
                .from(outputs)
                .into("")
            val args =
                project
                    .properties
                    .mapValues { (_, v) -> v.toString() }
                    .toMutableMap()
            args.putIfAbsent("JAR_FILE", outputs.files.singleFile.name)
            buildArgs(args)
            taskNameList =
                namedTags
                    .keys
                    .plus(project.version.toString())
                    .map { "dockerPush${it.replaceFirstChar { char -> char.uppercase() }}" }
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
