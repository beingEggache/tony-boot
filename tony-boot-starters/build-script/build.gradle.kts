import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.tony"
version = "0.1-SNAPSHOT"

val javaVersion: String by project
val kotlinVersion: String by project

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
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.1.5")
    implementation("com.palantir.gradle.docker:gradle-docker:0.35.0")
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
