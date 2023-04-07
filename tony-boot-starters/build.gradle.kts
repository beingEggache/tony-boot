import com.tony.buildscript.Deps
import com.tony.buildscript.KaptDeps
import com.tony.buildscript.VersionManagement
import com.tony.buildscript.addDepsManagement
import com.tony.buildscript.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("tony-build-dep-substitute") apply false
    idea
}

configure(allprojects) {
    group = projectGroup
    version = VersionManagement.templateVersion
    repositories {
        mavenLocal()

//        val privateMavenRepoUrl: String by project
//        maven(url = privateMavenRepoUrl) {
//            name = "private"
//            isAllowInsecureProtocol = true
//        }

        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
        maven(url = "https://maven.aliyun.com/repository/google")
        mavenCentral()
    }
}

val javaVersion: String by project
// copyProjectHookToGitHook(rootDir.parentFile,"pre-commit", "pre-push")
idea.project {
    jdkName = javaVersion
    languageLevel = IdeaLanguageLevel(JavaVersion.toVersion(javaVersion))
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
        constraints {
            addDepsManagement()
        }
        add("api", platform(Deps.SpringCloudDeps.springCloudDependencies))
        add("api", platform(Deps.SpringCloudDeps.springCloudAlibabaDenpendencies))
    }
}

configure(subprojects) {

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
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
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
}
