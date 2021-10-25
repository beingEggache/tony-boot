import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Version.kotlinVersion apply false
    kotlin("plugin.spring") version Version.kotlinVersion apply false
    idea
}

copyProjectHookToGitHook("pre-commit", "pre-push")

idea.project {
    jdkName = "11"
    languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_11)
    vcs = "Git"
}

configure(allprojects) {
    group = "com.tony"
    version = "0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        val privateMavenRepoUrl: String by project
        maven(url = privateMavenRepoUrl) {
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }
}

configure(listOf(rootProject)) {
    apply(plugin = "org.gradle.java-platform")
    apply(plugin = "org.gradle.maven-publish")

    configure<PublishingExtension> {
        repositories {
            maven {

                val releasesRepoUrl: String by project
                val snapshotsRepoUrl: String by project
                val nexusUsername: String by project
                val nexusPassword: String by project

                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                isAllowInsecureProtocol = true
                credentials {
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }
        publications {
            register("pom", MavenPublication::class) {
                from(components["javaPlatform"])
            }
        }
    }

    dependencies {
        constraints {
            DepManagement::class.nestedClasses.flatMap {
                it.staticFieldValues()
            }.forEach {
                add("api", it)
            }
        }
    }
}

configure(subprojects) {

    substituteDeps()

    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("ktlint")
        plugin("maven.publish")
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        add("implementation", platform(rootProject))
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
