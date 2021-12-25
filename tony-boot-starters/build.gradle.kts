import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    kotlin("jvm") version Version.kotlinVersion apply false
    kotlin("plugin.spring") version Version.kotlinVersion apply false
    kotlin("kapt") version Version.kotlinVersion apply false
    idea
}

copyProjectHookToGitHook("pre-commit", "pre-push")

idea.project {
    jdkName = "11"
    languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_11)
    vcs = "Git"
}

configure(allprojects) {

    group = projectGroup
    version = Version.templateVersion

    repositories {
        mavenLocal()
        val privateMavenRepoUrl: String by project
        maven(url = privateMavenRepoUrl) {
            name = "private"
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }
}

configure(listOf(rootProject)) {

    ext.set("pom", true)

    apply {
        plugin("org.gradle.java-platform")
        plugin("maven.publish")
    }

    dependencies {
        constraints {
            addDepsManagement()
        }
    }
}
configure(subprojects) {

    substituteDeps()

    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-kapt")
        plugin("ktlint")
        plugin("maven.publish")
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(11))
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
        }
    }

    dependencies {
        add("implementation", platform(rootProject))
        add("kapt", DepsManagement.SpringBoot.configurationProcessor)
        add("kapt", DepsManagement.SpringBoot.autoconfigureProcessor)
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
