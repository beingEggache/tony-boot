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
        plugin("ktlint")
    }

    dependencies {
        add("implementation", platform(Deps.Tony.templateDependencies))
        add("implementation", platform(Deps.SpringCloudDeps.springCloudDependencies))
        add("implementation", platform(Deps.SpringCloudDeps.springCloudAlibabaDenpendencies))
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(11))
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
