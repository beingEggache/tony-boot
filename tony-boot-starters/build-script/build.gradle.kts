import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.tony"
version = "0.1-SNAPSHOT"

val javaVersion: String by project

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = javaVersion
        allWarningsAsErrors = true
        verbose = true
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xjvm-default=all",
            "-Werror",
            "-verbose",
            "-version",
            "-progressive",
//                "-deprecation",
//                "-Xlint:all",
//                "-encoding UTF-8",
        )
    }
}

repositories {
    mavenLocal()
    maven(url = "https://maven.aliyun.com/repository/public")
    gradlePluginPortal()
//    val privateGradleRepoUrl: String by project
//    maven(url = privateGradleRepoUrl) {
//        isAllowInsecureProtocol = true
//    }
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("com.tony.build.dep-substitute") {
            id = "com.tony.build.dep-substitute"
            implementationClass = "com.tony.buildscript.SubstituteDepsPlugin"
        }

        create("com.tony.build.ktlint") {
            id = "com.tony.build.ktlint"
            implementationClass = "com.tony.buildscript.KtlintPlugin"
        }

        create("com.tony.build.maven-publish") {
            id = "com.tony.build.maven-publish"
            implementationClass = "com.tony.buildscript.MavenPublishPlugin"
        }

        create("com.tony.build.docker") {
            id = "com.tony.build.docker"
            implementationClass = "com.tony.buildscript.DockerPlugin"
        }
    }
}
dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.12")
    implementation("com.palantir.gradle.docker:gradle-docker:0.35.0")
}

configure<JavaPluginExtension> {
    withSourcesJar()
    withJavadocJar()
}

val releasesGradleRepoUrl: String by project
val snapshotsGradleRepoUrl: String by project
val nexusUsername: String by project
val nexusPassword: String by project

configure<PublishingExtension> {
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
