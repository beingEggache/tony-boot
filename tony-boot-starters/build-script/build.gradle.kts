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
            "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation -version -progressive"
        )
    }
}

val privateGradleRepoUrl: String by project
repositories {
    mavenLocal()
    gradlePluginPortal()
    maven(url = privateGradleRepoUrl) {
        isAllowInsecureProtocol = true
    }
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("tony-build-dep-substitute") {
            id = "tony-build-dep-substitute"
            implementationClass = "com.tony.buildscript.SubstituteDepsPlugin"
        }

        create("tony-build-ktlint") {
            id = "tony-build-ktlint"
            implementationClass = "com.tony.buildscript.KtlintPlugin"
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.10")
    implementation("io.github.godfather1103:docker-plugin:1.2.4")
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
