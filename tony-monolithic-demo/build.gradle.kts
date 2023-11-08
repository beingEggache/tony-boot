import com.tony.gradle.Deps
import com.tony.gradle.KaptDeps
import com.tony.gradle.addTestDependencies
import com.tony.gradle.projectGroup
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    base
}

val javaVersion: String by project

// copyProjectHookToGitHook(rootDir.parentFile, "pre-commit", "pre-push")

val kotlinVersion: String by project
configure(subprojects) {
    group = projectGroup
    version = "0.1"
    repositories {
        mavenLocal()

//        val privateMavenRepoUrl: String by project
//        maven(url = privateMavenRepoUrl) {
//            name = "private"
//            isAllowInsecureProtocol = true
//        }
        maven(url = "https://maven.aliyun.com/repository/public")
        mavenCentral()
    }

    apply {
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("com.tony.gradle.plugin.ktlint")
        plugin("com.tony.gradle.plugin.dep-configurations")
    }

    tasks.withType<Javadoc> {
        this.enabled = false
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion))
            languageVersion.set(KotlinVersion.fromVersion(kotlinVersion.substring(0..2)))
            verbose.set(true)
            progressiveMode.set(true)
            freeCompilerArgs.addAll(
                "-Xjsr305=strict",
                "-Xjvm-default=all",
            )
        }
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
        add("kapt", KaptDeps.Spring.contextIndexer)
        addTestDependencies()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
