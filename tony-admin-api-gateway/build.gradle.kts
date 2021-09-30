import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Version.kotlinVersion apply false
    kotlin("plugin.spring") version Version.kotlinVersion apply false
    kotlin("kapt") version Version.kotlinVersion apply false
}

copyProjectHookToGitHook("pre-commit", "pre-push")

configure(subprojects) {
    group = "com.tony"
    version = "0.1-SNAPSHOT"

    val privateMavenRepoUrl: String by project
    repositories {
        mavenLocal()
        maven(url = privateMavenRepoUrl) {
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }
    forceDepsVersion()

    apply {
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("ktlint")
    }

    dependencies {
        add("kapt",Deps.Spring.contextIndexer)
    }

    tasks.withType<KotlinCompile>().configureEach {
        val isTest = this.name.contains("test", ignoreCase = true)
        kotlinOptions {
            jvmTarget = "11"
            allWarningsAsErrors = !isTest
            verbose = true
            freeCompilerArgs = listOf(
                "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation -version -progressive"
            )
        }
    }
}
