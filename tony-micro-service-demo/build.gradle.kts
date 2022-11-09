import com.tony.buildscript.Deps
import com.tony.buildscript.KaptDeps
import com.tony.buildscript.copyProjectHookToGitHook
import com.tony.buildscript.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
}

buildscript {
    dependencies {
        classpath("com.tony:build-script:0.1-SNAPSHOT")
    }
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
        mavenCentral()
        maven(url = privateMavenRepoUrl) {
            name = "private"
            isAllowInsecureProtocol = true
        }
    }

    apply {
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("tony-build-ktlint")
        plugin("tony-build-dep-substitute")
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
        add("kapt", KaptDeps.SpringBoot.autoconfigureProcessor)
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
