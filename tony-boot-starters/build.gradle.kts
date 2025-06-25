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
import tony.gradle.plugin.Build.Companion.templateGroup
import tony.gradle.plugin.Build.Companion.templatePrefix
import tony.gradle.plugin.Build.Companion.templateVersion
import org.cadixdev.gradle.licenser.LicenseExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
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
    alias(tonyLibs.plugins.licenser) apply false
}

val dependenciesProjects = setOf(project("${templatePrefix()}-dependencies"))
val dependenciesCatalogProjects = setOf(project("${templatePrefix()}-dependencies-catalog"))
val libraryProjects = subprojects - dependenciesProjects - dependenciesCatalogProjects

val javaVersion: String = rootProject.tonyLibs.versions.java.get()
val kotlinVersion: String = rootProject.tonyLibs.versions.kotlin.get()

configure(allprojects) {
    group = templateGroup()
    version = templateVersion()
    repositories {
        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/central")
        mavenCentral()
    }

    apply {
        plugin(rootProject.tonyLibs.plugins.licenser.get().pluginId)
    }

    extensions.getByType<LicenseExtension>().apply {
        this.setHeader(rootProject.file("LICENSE"))
        include(
            "**/*.java",
            "**/*.kt",
            "**/*.kts",
            "**/*.xml",
            "**/*.properties",
            "**/*.toml",
        )
    }
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
            .contains(Regex("alpha|beta|rc|snapshot|milestone|pre|m", RegexOption.IGNORE_CASE))
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

    tasks.withType<Jar> {
        into("META-INF") {
            from(rootProject.file("LICENSE"))
        }
        into("META-INF/maven/${project.group}/${project.name}") {
            from(project.tasks.getByPath("generatePomFileForJarPublication"))
            rename("pom-default.xml","pom.xml")
        }
        manifest {
            attributes["Implementation-Title"] = project.name
            attributes["Implementation-Version"] = project.version
            attributes["Build-Jdk-Spec"] = javaVersion
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    tasks.withType<Javadoc> {
        this.enabled = false
    }

    extensions.getByType<KotlinJvmProjectExtension>().apply {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
        }
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(javaVersion)
            languageVersion = KotlinVersion.fromVersion(kotlinVersion.substring(0..2))
            apiVersion= KotlinVersion.fromVersion(kotlinVersion.substring(0..2))
            jvmDefault = JvmDefaultMode.NO_COMPATIBILITY

            verbose = true
            progressiveMode = true
            extraWarnings = true
            // use kotlinc -X get more info.
            freeCompilerArgs.addAll(
                "-Xconsistent-data-class-copy-visibility",
                "-Xno-param-assertions",
                "-Xno-call-assertions",

                "-Xlambdas=indy",
                "-Xsam-conversions=indy",
                "-Xstring-concat=indy-with-constants",

                "-Xreport-all-warnings",

                "-Xjsr305=strict",
                "-Xenhance-type-parameter-types-to-def-not-null",

                "-Xjspecify-annotations=strict",
                "-Xtype-enhancement-improvements-strict-mode",
                // "-Xuse-fast-jar-file-system",
            )
        }
        explicitApi()
    }

    dependencies {
        add("implementation", platform(project(":${templatePrefix()}-dependencies")))
        add("kapt", rootProject.tonyLibs.bundles.springBootProcessors)
        add("kaptTest", rootProject.tonyLibs.springContextIndexer)
        add("testImplementation", rootProject.tonyLibs.bundles.test)
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }

    // fix kapt additional-spring-configuration-metadata.json can not process problem
    extensions.getByType<KaptExtension>().apply {
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
