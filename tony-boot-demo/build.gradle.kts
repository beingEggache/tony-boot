import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Version.kotlinVersion apply false
    kotlin("plugin.spring") version Version.kotlinVersion apply false
}

forceDepsVersion()

configure(subprojects) {
    group = "com.tony"
    version = "0.1"

    val aliyunMavenRepoUrl: String by project
    val privateMavenRepoUrl: String by project

    repositories {
        mavenCentral()
        maven(url = privateMavenRepoUrl) {
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        maven(url = aliyunMavenRepoUrl) {
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
    }

    apply {
        plugin("kotlin")
        plugin("ktlint")
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
