import com.tony.gradle.plugin.Build
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `version-catalog`
    alias(tonyLibs.plugins.tonyGradleBuild)
    alias(tonyLibs.plugins.kotlin) apply false
    alias(tonyLibs.plugins.kotlinSpring) apply false
    alias(tonyLibs.plugins.kotlinKapt) apply false
}

val dependenciesProjects = setOf(project("${Build.PREFIX}-dependencies"))
val dependenciesCatalogProjects = setOf(project("${Build.PREFIX}-dependencies-catalog"))
val libraryProjects = subprojects - dependenciesProjects - dependenciesCatalogProjects

val javaVersion: String = rootProject.tonyLibs.versions.java.get()
val kotlinVersion: String = rootProject.tonyLibs.versions.kotlin.get()

configure(subprojects) {
    group = Build.GROUP
    version = Build.VERSION
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
    tasks.withType<Jar> {
        manifest {
            attributes["Implementation-Title"] = project.name
            attributes["Implementation-Version"] = project.version
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

// copyProjectHookToGitHook(rootDir.parentFile,"pre-commit", "pre-push")

configure(dependenciesProjects) {
    ext.set("pom", true)
    apply {
        plugin("org.gradle.java-platform")
        plugin("com.tony.gradle.plugin.maven-publish")
    }
    extensions.getByType<JavaPlatformExtension>().apply {
        allowDependencies()
    }
}

configure(dependenciesCatalogProjects) {
    ext.set("catalog", true)
    apply {
        plugin("org.gradle.version-catalog")
        plugin("com.tony.gradle.plugin.maven-publish")
    }
}

configure(libraryProjects) {

    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-kapt")
        plugin("com.tony.gradle.plugin.ktlint")
        plugin("com.tony.gradle.plugin.dep-configurations")
        plugin("com.tony.gradle.plugin.maven-publish")
    }
    tasks.withType<Javadoc> {
        this.enabled = false
    }

    extensions.getByType<KotlinJvmProjectExtension>().apply {

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
        explicitApi()
    }

    dependencies {
        add("implementation", platform(project(":${Build.PREFIX}-dependencies")))
        add("kapt", rootProject.tonyLibs.bundles.springBootProcessors)
        add("kaptTest", rootProject.tonyLibs.springContextIndexer)
        add("testImplementation", rootProject.tonyLibs.bundles.test)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
