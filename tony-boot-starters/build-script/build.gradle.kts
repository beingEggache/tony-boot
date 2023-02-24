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
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(javaVersion))
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

repositories {
    mavenCentral()
    val privateGradleRepoUrl: String by project
    maven(url = privateGradleRepoUrl) {
        isAllowInsecureProtocol = true
    }
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
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
    implementation("com.palantir.gradle.docker:gradle-docker:0.34.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.9")
}

configure<JavaPluginExtension> {
    withSourcesJar()
    withJavadocJar()
}

configure<PublishingExtension> {
    repositories {

        maven {
            name = "privateGradle"
            val releasesGradleRepoUrl: String by project
            val snapshotsGradleRepoUrl: String by project
            val nexusUsername: String by project
            val nexusPassword: String by project

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
