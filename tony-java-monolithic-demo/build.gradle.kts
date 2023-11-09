import com.tony.gradle.plugin.Build

plugins {
    alias(tonyLibs.plugins.tonyGradleBuild)
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
            "-Xlint:all,-processing,-unchecked",
            "-Xdoclint:all,-missing",
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
    }
}
