package com.tony.buildscript

import com.github.godfather1103.gradle.entity.AuthConfig
import com.github.godfather1103.gradle.entity.Resource
import com.github.godfather1103.gradle.ext.DockerPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import java.text.SimpleDateFormat
import java.util.*

class DockerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.apply {
            plugin("io.github.godfather1103.docker-plugin")
            plugin("org.springframework.boot")
        }

        val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
        val dockerRegistry: String by project
        val dockerUserName: String by project
        val dockerPassword: String by project
        val imageNameFromProperty = project.getImageNameFromProperty()
        val nameSpace: String = kotlin.run {
            val property = System.getProperty("name_space", "")
            if (property.isNullOrBlank()) dockerUserName else property
        }

        project.configure<DockerPluginExtension> {
            dockerBuildDependsOn.add("bootJar")
            val outputs = project.tasks.getByPath("bootJar").outputs
            dockerBuildArgs.put("JAR_FILE", outputs.files.singleFile.name)
            dockerDirectory.value(project.projectDir.absolutePath)
            imageName.value("$dockerRegistry/${nameSpace}/${imageNameFromProperty}")
            val today = yyyyMMdd.format(Date())
            dockerImageTags.add("latest")
            dockerImageTags.add(today)
            if (!System.getProperty("push").isNullOrBlank()) {
                pushImageTag.value(true)
                pushImage.value(true)
                auth.value(AuthConfig(dockerUserName, dockerPassword))
            } else {
                project.logger.info("skipDockerPush")
                skipDockerTag.value(true)
                skipDockerPush.value(true)
            }

            val resource = Resource()
            resource.directory = project.projectDir.absolutePath + "/build/libs"
            resource.addIncludes(outputs.files.singleFile.name)
            resources.add(resource)
        }
    }
}
