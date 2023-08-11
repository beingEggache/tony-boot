import com.tony.buildscript.Deps
import com.tony.buildscript.KaptDeps
import com.tony.buildscript.addTestDependencies
import com.tony.buildscript.getProfile
import com.tony.buildscript.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    id("io.freefair.lombok") version "8.1.0"
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
        mavenCentral()
    }

    apply {
        plugin("org.gradle.java-library")
        plugin("io.freefair.lombok")
        plugin("com.tony.build.dep-configurations")
    }

    dependencies {
        add("implementation", platform(Deps.Template.templateDependencies))
        add("annotationProcessor", KaptDeps.Spring.contextIndexer)
        addTestDependencies()
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    tasks.withType<JavaCompile>().configureEach {
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
}
