import com.tony.buildscript.Deps
import com.tony.buildscript.KaptDeps
import com.tony.buildscript.VersionManagement
import com.tony.buildscript.addDepsManagement
import com.tony.buildscript.addTestDependencies
import com.tony.buildscript.projectGroup
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.tony.build.dep-configurations") apply false
    idea
}

configure(allprojects) {
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
        plugin("com.tony.build.maven-publish")
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

val kotlinVersion: String by project
configure(subprojects) {

    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-kapt")
        plugin("com.tony.build.ktlint")
        plugin("com.tony.build.dep-configurations")
        plugin("com.tony.build.maven-publish")
    }

    tasks.withType<Javadoc>().configureEach {
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
        add("implementation", platform(rootProject))

        add("kapt", KaptDeps.SpringBoot.configurationProcessor)
        add("kapt", KaptDeps.SpringBoot.autoconfigureProcessor)
        add("kapt", KaptDeps.Spring.contextIndexer)

        add("kaptTest", KaptDeps.Spring.contextIndexer)
        addTestDependencies()
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}
