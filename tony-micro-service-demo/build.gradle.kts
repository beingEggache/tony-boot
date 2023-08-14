import com.tony.buildscript.Deps
import com.tony.buildscript.KaptDeps
import com.tony.buildscript.addTestDependencies
import com.tony.buildscript.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
}

val javaVersion:String by project

// copyProjectHookToGitHook(rootDir.parentFile,"pre-commit", "pre-push")

idea.project {
    jdkName = javaVersion
    languageLevel = IdeaLanguageLevel(JavaVersion.toVersion(javaVersion))
    vcs = "Git"
}

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
        plugin("com.tony.build.ktlint")
        plugin("com.tony.build.dep-configurations")
    }

    tasks.withType<Javadoc>().configureEach {
        this.enabled = false
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
            javaParameters = true
            verbose = true
            allWarningsAsErrors = !isTest
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xjvm-default=all",
                "-verbose",
                "-version",
                "-progressive",
                "-Werror",
            )
        }
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
        add("kapt", KaptDeps.Spring.contextIndexer)
        addTestDependencies()
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}
