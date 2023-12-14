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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.tony"
version = "0.1-SNAPSHOT"

val versionCatalog = versionCatalogs.named("tonyLibs")

val javaVersion: String = versionCatalog.findVersion("java").get().toString()
val kotlinVersion: String = versionCatalog.findVersion("kotlin").get().toString()

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    withSourcesJar()
    withJavadocJar()
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
    }
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaVersion))
        languageVersion.set(KotlinVersion.fromVersion(kotlinVersion.substring(0..2)))
        verbose.set(true)
        progressiveMode.set(true)
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xjvm-default=all",
        )
    }
}

repositories {
    mavenLocal()
    gradlePluginPortal()
//    val privateGradleRepoUrl: String by project
//    maven(url = privateGradleRepoUrl) {
//        isAllowInsecureProtocol = true
//    }
    maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("com.tony.gradle.plugin.build") {
            id = "com.tony.gradle.plugin.build"
            implementationClass = "com.tony.gradle.plugin.Build"
        }

        create("com.tony.gradle.plugin.dep-configurations") {
            id = "com.tony.gradle.plugin.dep-configurations"
            implementationClass = "com.tony.gradle.plugin.DependenciesConfigurationsPlugin"
        }

        create("com.tony.gradle.plugin.ktlint") {
            id = "com.tony.gradle.plugin.ktlint"
            implementationClass = "com.tony.gradle.plugin.KtlintPlugin"
        }

        create("com.tony.gradle.plugin.maven-publish") {
            id = "com.tony.gradle.plugin.maven-publish"
            implementationClass = "com.tony.gradle.plugin.MavenPublishPlugin"
        }

        create("com.tony.gradle.plugin.docker") {
            id = "com.tony.gradle.plugin.docker"
            implementationClass = "com.tony.gradle.plugin.DockerPlugin"
        }
    }
}

dependencies {
    implementation(versionCatalog.findLibrary("springBootGradlePlugin").get())
    implementation(versionCatalog.findLibrary("gradleDocker").get())
}

val releasesGradleRepoUrl: String by project
val snapshotsGradleRepoUrl: String by project
val nexusUsername: String by project
val nexusPassword: String by project

publishing {
    repositories {
        maven {
            name = "privateGradle"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsGradleRepoUrl else releasesGradleRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
    publications {
        register("jar", MavenPublication::class) {
            from(components["kotlin"])
        }

        register("jarAndSrc", MavenPublication::class) {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
        }
    }
}
