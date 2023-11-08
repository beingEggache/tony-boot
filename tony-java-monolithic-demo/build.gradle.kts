import com.tony.gradle.Deps
import com.tony.gradle.KaptDeps
import com.tony.gradle.addTestDependencies
import com.tony.gradle.getProfile
import com.tony.gradle.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    id("io.freefair.lombok") version "8.4"
}

val javaVersion: String by project

// copyProjectHookToGitHook(rootDir.parentFile, "pre-commit", "pre-push")

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
        plugin("org.gradle.java-library")
        plugin("io.freefair.lombok")
        plugin("com.tony.gradle.plugin.dep-configurations")
    }

    tasks.withType<Javadoc> {
        this.enabled = false
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        options.encoding = "UTF-8"
        options.isDeprecation = true
//        options.isVerbose = true
        if (getProfile() != "prod") {
            options.isDebug = true
            options.debugOptions.debugLevel = "vars"
            options.isFork = true
            options.forkOptions.jvmArgs?.add("-Duser.language=en")
            options.isIncremental = true
        } else {
            options.isDebug = false
        }
        options.compilerArgs = mutableListOf(
//            "-Xlint:all,-serial,-processing,-classfile,-unchecked",
            "-Xlint:all,-processing,-unchecked",
            "-Xdoclint:all,-missing",
            "-Werror"
        )
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
        add("annotationProcessor", KaptDeps.Spring.contextIndexer)
        addTestDependencies()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
