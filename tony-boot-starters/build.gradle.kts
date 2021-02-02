import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") version "1.4.21" apply false
//    id("io.gitlab.arturbosch.detekt") version "1.14.0" apply false
}

forceDepsVersion()
copyProjectHookToGitHook("pre-commit","pre-push")

configure(subprojects) {
    group = "com.tony"
    version = "0.1-SNAPSHOT"

    //avoid UnknownPropertyException
    if (!extra.has("profile")) {
        extra.set("profile", "dev")
    }

    val privateMavenRepoUrl: String by project
    val aliyunMavenRepoUrl: String by project

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
        plugin("ktlint")
//        plugin("io.gitlab.arturbosch.detekt")
    }


//    tasks.withType<Detekt>().configureEach {
//        // Target version of the generated JVM bytecode. It is used for type resolution.
//        this.jvmTarget = "11"
//        this.parallel = true
//        this.ignoreFailures = true
//        this.config.from("$rootDir/detekt-config.yaml")
//        this.reports {
//            this.html {
//                enabled = true
//                destination = file("build/reports/detekt.html")
//            }
//        }
//    }

    tasks.withType<KotlinCompile>().configureEach {
        val isTest = this.name.contains("test", ignoreCase = true)
        kotlinOptions {
            languageVersion = "1.4"
            jvmTarget = "11"
            allWarningsAsErrors = !isTest
            verbose = true
            freeCompilerArgs = listOf(
                "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation"
            )
        }
    }
}
