import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    kotlin("jvm") version Version.kotlinVersion apply false
    kotlin("plugin.spring") version Version.kotlinVersion apply false
    kotlin("kapt") version Version.kotlinVersion apply false
    idea
}

val javaVersion:String by project

copyProjectHookToGitHook("pre-commit", "pre-push")

idea.project {
    jdkName = javaVersion
    languageLevel = IdeaLanguageLevel(JavaVersion.toVersion(javaVersion))
    vcs = "Git"
}

configure(subprojects) {
    group = projectGroup
    version = "0.1"

    val privateMavenRepoUrl: String by project
    repositories {
        mavenLocal()
        maven(url = privateMavenRepoUrl) {
            name = "private"
            @Suppress("UnstableApiUsage")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }

    substituteDeps()

    apply {
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("ktlint")
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
        add("implementation", platform(Deps.SpringCloudDeps.springCloudDependencies))
        add("implementation", platform(Deps.SpringCloudDeps.springCloudAlibabaDenpendencies))
        add("kapt", Deps.SpringBoot.autoconfigureProcessor)
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion))
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        val isTest = this.name.contains("test", ignoreCase = true)
        kotlinOptions {
            jvmTarget = javaVersion
            allWarningsAsErrors = !isTest
            verbose = true
            freeCompilerArgs = listOf(
                "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation -version -progressive"
            )
        }
    }
}
