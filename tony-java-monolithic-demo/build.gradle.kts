import com.tony.buildscript.Deps
import com.tony.buildscript.getProfile
import com.tony.buildscript.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
	id("io.freefair.lombok") version "6.6.3"
}

val javaVersion: String by project

// copyProjectHookToGitHook(rootDir.parentFile, "pre-commit", "pre-push")

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
        maven(url = "https://maven.aliyun.com/repository/jcenter")
        maven(url = "https://maven.aliyun.com/repository/google")

        mavenCentral()
    }

    apply {
        plugin("kotlin")
		plugin("io.freefair.lombok")
        plugin("tony-build-dep-substitute")
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        options.encoding = "UTF-8"
        options.isDeprecation = true
        if (getProfile() != "prod") {
            options.isDebug = true
            options.isFork = true
            options.forkOptions.jvmArgs?.add("-Duser.language=en")
            options.isIncremental = true
        } else {
            options.isDebug = false
        }

        options.compilerArgs = mutableListOf(
            "-Xlint:all,-serial,-processing,-classfile,-unchecked",
            "-Werror"
        )
    }
}
