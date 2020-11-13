import com.tony.build.forceDepsVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") version "1.4.10" apply false
}

forceDepsVersion()

configure(subprojects) {
    group = "com.tony"
    version = "0.1"

    //avoid UnknownPropertyException
    if (!extra.has("profile")) {
        extra.set("profile", "dev")
    }

    val aliyunMavenRepoUrl: String by project
    val privateMavenRepoUrl: String by project

    repositories {
        mavenLocal()
        maven(url = aliyunMavenRepoUrl) {
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        maven(url = privateMavenRepoUrl) {
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }

    apply {
        plugin("kotlin")
    }

    tasks.withType<KotlinCompile>().configureEach {
        val isTest = this.name.contains("test", ignoreCase = true)
        kotlinOptions {
            jvmTarget = "11"
            allWarningsAsErrors = !isTest
            verbose = true
            freeCompilerArgs = listOf("-Xjsr305=strict -Xlint:all -verbose -encoding=UTF8")
        }
    }
}
