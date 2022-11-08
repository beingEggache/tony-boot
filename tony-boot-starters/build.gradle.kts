import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import com.tony.buildscript.addDepsManagement
import com.tony.buildscript.Deps
import com.tony.buildscript.projectGroup
import com.tony.buildscript.VersionManagement
import com.tony.buildscript.KaptDeps
import com.tony.buildscript.copyProjectHookToGitHook

plugins {
    kotlin("jvm") version "1.7.20" apply false
    kotlin("plugin.spring") version "1.7.20" apply false
    kotlin("kapt") version "1.7.20" apply false
    id("tony-build-dep-substitute") apply false
    idea
}


configure(allprojects) {

    group = projectGroup
    version = VersionManagement.templateVersion

    repositories {
        mavenLocal()
        mavenCentral()
        val privateMavenRepoUrl: String by project
        maven(url = privateMavenRepoUrl) {
            name = "private"
            isAllowInsecureProtocol = true
        }
    }
}

val javaVersion:String by project
copyProjectHookToGitHook("pre-commit", "pre-push")
idea.project {
    jdkName = javaVersion
    languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_11)
    vcs = "Git"
}

configure(listOf(rootProject)) {

    ext.set("pom", true)

    apply {
        plugin("org.gradle.java-platform")
        plugin("maven.publish")
    }

    configure<JavaPlatformExtension> {
        allowDependencies()
    }

    dependencies {
        add("api", platform(Deps.SpringCloudDeps.springCloudDependencies))
        add("api", platform(Deps.SpringCloudDeps.springCloudAlibabaDenpendencies))

        constraints {
            addDepsManagement()
        }
    }
}

val moduleProjects = subprojects
configure(moduleProjects) {

    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-kapt")
        plugin("tony-build-ktlint")
        plugin("maven.publish")
        plugin("tony-build-dep-substitute")
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
        explicitApi()
    }

    dependencies {
        add("implementation", platform(rootProject))
        add("kapt", KaptDeps.SpringBoot.configurationProcessor)
        add("kapt", KaptDeps.SpringBoot.autoconfigureProcessor)
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
