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

import com.tony.gradle.plugin.Build
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `version-catalog`
    alias(tonyLibs.plugins.tonyGradleBuild)
    alias(tonyLibs.plugins.kotlin) apply false
    alias(tonyLibs.plugins.kotlinSpring) apply false
    alias(tonyLibs.plugins.kotlinKapt) apply false
    alias(tonyLibs.plugins.dokka)
}

val dependenciesProjects = setOf(project("${Build.PREFIX}-dependencies"))
val dependenciesCatalogProjects = setOf(project("${Build.PREFIX}-dependencies-catalog"))
val libraryProjects = subprojects - dependenciesProjects - dependenciesCatalogProjects

val javaVersion: String = rootProject.tonyLibs.versions.java.get()
val kotlinVersion: String = rootProject.tonyLibs.versions.kotlin.get()

configure(allprojects) {
    group = Build.GROUP
    version = Build.VERSION
    repositories {
        mavenLocal()

//        val privateMavenRepoUrl: String by project
//        maven(url = privateMavenRepoUrl) {
//            name = "private"
//            isAllowInsecureProtocol = true
//        }

        maven(url = "https://maven.aliyun.com/repository/public")
        mavenCentral()
    }
    tasks.withType<Jar> {
        manifest {
            attributes["Implementation-Title"] = project.name
            attributes["Implementation-Version"] = project.version
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

// copyProjectHookToGitHook(rootDir.parentFile,"pre-commit", "pre-push")

configure(dependenciesProjects) {
    ext.set("pom", true)
    apply {
        plugin("org.gradle.java-platform")
        plugin("com.tony.gradle.plugin.maven-publish")
    }
    extensions.getByType<JavaPlatformExtension>().apply {
        allowDependencies()
    }
}

configure(dependenciesCatalogProjects) {
    ext.set("catalog", true)
    apply {
        plugin("org.gradle.version-catalog")
        plugin("com.tony.gradle.plugin.maven-publish")
    }
}

tasks.dokkaHtmlMultiModule {
    moduleName.set(rootProject.name)
    moduleVersion.set(rootProject.version.toString())
}

configure(libraryProjects) {

    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-kapt")
        plugin("com.tony.gradle.plugin.ktlint")
        plugin("com.tony.gradle.plugin.dep-configurations")
        plugin("com.tony.gradle.plugin.maven-publish")
        plugin("org.jetbrains.dokka")
    }

    tasks.withType<Javadoc> {
        this.enabled = false
    }

    extensions.getByType<KotlinJvmProjectExtension>().apply {

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
        explicitApi()
    }

    dependencies {
        add("implementation", platform(project(":${Build.PREFIX}-dependencies")))
        add("kapt", rootProject.tonyLibs.bundles.springBootProcessors)
        add("kaptTest", rootProject.tonyLibs.springContextIndexer)
        add("testImplementation", rootProject.tonyLibs.bundles.test)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
        jvmArgs =
            listOf(
                "--add-opens=java.base/java.util=ALL-UNNAMED",
                "-Dlogging.config=${rootProject.rootDir}/config/logback-spring.xml",
                "-XX:+EnableDynamicAgentLoading"
            )
    }
}
