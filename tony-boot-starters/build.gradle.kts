import com.tony.gradle.KaptDeps
import com.tony.gradle.VersionManagement
import com.tony.gradle.addTestDependencies
import com.tony.gradle.projectGroup
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.tony.gradle.plugin.build")
}

val projectPrefix: String by project
val dependenciesProject = project("$projectPrefix-dependencies")
val libraryProjects = subprojects - dependenciesProject

configure(subprojects) {
    group = projectGroup
    version = VersionManagement.TEMPLATE_VERSION
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

val javaVersion: String by project
// copyProjectHookToGitHook(rootDir.parentFile,"pre-commit", "pre-push")

configure(listOf(dependenciesProject)) {
    ext.set("pom", true)
    apply {
        plugin("org.gradle.java-platform")
        plugin("com.tony.gradle.plugin.maven-publish")
    }
    configure<JavaPlatformExtension> {
        allowDependencies()
    }
}
val libraries = libs
val kotlinVersion: String by project

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

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
        }
        compilerOptions  {
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
        add("implementation", platform(dependenciesProject))
        add("kapt", rootProject.libs.springBootConfigurationProcessor)
        add("kapt", rootProject.libs.springBootAutoconfigureProcessor)
        add("kapt", rootProject.libs.springContextIndexer)
        add("kaptTest", rootProject.libs.springContextIndexer)
        addTestDependencies()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
