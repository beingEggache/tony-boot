import com.tony.gradle.plugin.Build
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(tonyLibs.plugins.lombok)
}

val javaVersion: String = rootProject.tonyLibs.versions.java.get()

// copyProjectHookToGitHook(rootDir.parentFile, "pre-commit", "pre-push")

configure(subprojects) {
    group = Build.GROUP
    version = "0.1"
    repositories {
        mavenLocal()

//        val privateMavenRepoUrl: String by project
//        maven(url = privateMavenRepoUrl) {
//            name = "private"
//            isAllowInsecureProtocol = true
//        }
        maven(url = "https://maven.oschina.net/content/groups/public/")
        mavenCentral()
    }

    apply {
        plugin("org.gradle.java-library")
        plugin("io.freefair.lombok")
        plugin(rootProject.tonyLibs.plugins.tonyDepConfigurations.get().pluginId)
    }

    tasks.withType<Javadoc> {
        this.enabled = false
    }

    extensions.getByType<JavaPluginExtension>().apply {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        options.encoding = "UTF-8"
        options.isDeprecation = true
//        options.isVerbose = true
        if (Build.getProfile() != "prod") {
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
            "-Xlint:all,-processing",
            "-Werror"
        )
    }

    dependencies {
        add("implementation", platform(Build.templateProject("dependencies")))
        add("annotationProcessor", rootProject.tonyLibs.springContextIndexer)
        add("testImplementation", rootProject.tonyLibs.bundles.test)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
        listOf("-XX:+EnableDynamicAgentLoading")
    }
}
