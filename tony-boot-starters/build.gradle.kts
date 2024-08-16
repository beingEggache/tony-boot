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

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.tony.gradle.plugin.Build
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

plugins {
    `version-catalog`
    alias(tonyLibs.plugins.tonyGradleBuild)
    alias(tonyLibs.plugins.kotlin) apply false
    alias(tonyLibs.plugins.kotlinSpring) apply false
    alias(tonyLibs.plugins.kotlinKapt) apply false
    alias(tonyLibs.plugins.dokka)
    alias(tonyLibs.plugins.gradleVersionsPlugin)
    alias(tonyLibs.plugins.dependencyAnalysis)
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

        maven(url = "https://maven.aliyun.com/repository/central")
        mavenCentral()
    }
    tasks.withType<Jar> {
        manifest {
            attributes["Implementation-Title"] = project.name
            attributes["Implementation-Version"] = project.version
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    tasks.withType<DependencyUpdatesTask> {
        revision = "release"
        checkForGradleUpdate = true
        gradleReleaseChannel = "current"
        checkConstraints = true
        checkBuildEnvironmentConstraints = true
        outputFormatter = "plain"
        rejectVersionIf {
            candidate
                .version
                .contains(Regex("alpha|beta|rc|snapshot|milestone|pre|m",RegexOption.IGNORE_CASE))
        }
    }
}

// copyProjectHookToGitHook(rootDir.parentFile,"pre-commit", "pre-push")

configure(dependenciesProjects) {
    ext.set("pom", true)
    apply {
        plugin(rootProject.tonyLibs.plugins.javaPlatform.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyMavenPublish.get().pluginId)
    }
    extensions.getByType<JavaPlatformExtension>().apply {
        allowDependencies()
    }
}

configure(dependenciesCatalogProjects) {
    ext.set("catalog", true)
    apply {
        plugin(rootProject.tonyLibs.plugins.versionCatalog.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyMavenPublish.get().pluginId)
    }
}

tasks.dokkaHtmlMultiModule {
    moduleName.set(rootProject.name)
    moduleVersion.set(rootProject.version.toString())
}

configure(libraryProjects) {

    apply {
        plugin(rootProject.tonyLibs.plugins.kotlin.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.kotlinKapt.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyKtlint.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyDepConfigurations.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyMavenPublish.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.dokka.get().pluginId)
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
            // use kotlinc -X get more info.
            freeCompilerArgs.addAll(
                "-Xjsr305=strict",
                "-Xjvm-default=all",
                "-Xlambdas=indy",
                "-Xsam-conversions=indy",
                "-Xjspecify-annotations=strict",
                "-Xtype-enhancement-improvements-strict-mode",
                "-Xenhance-type-parameter-types-to-def-not-null",
                "-Xextended-compiler-checks",
                // "-Xuse-fast-jar-file-system",
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

    // fix kapt additional-spring-configuration-metadata.json can not process problem
    configure<KaptExtension> {
        arguments {
            arg(
                "org.springframework.boot.configurationprocessor.additionalMetadataLocations",
                "$projectDir/src/main/resources"
            )
        }
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
