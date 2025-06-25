import tony.gradle.plugin.Build.Companion.templateProject
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(tonyLibs.plugins.kotlin) apply false
    alias(tonyLibs.plugins.kotlinSpring) apply false
    alias(tonyLibs.plugins.kotlinKapt) apply false
}

val javaVersion: String = rootProject.tonyLibs.versions.java.get()
val kotlinVersion: String = rootProject.tonyLibs.versions.kotlin.get()

configure(subprojects) {
    version = "0.1"
    repositories {
        mavenLocal()

//        val privateMavenRepoUrl: String by project
//        maven(url = privateMavenRepoUrl) {
//            name = "private"
//            isAllowInsecureProtocol = true
//        }
        maven(url = "https://maven.aliyun.com/repository/central")
        mavenCentral()
    }

    apply {
        plugin(rootProject.tonyLibs.plugins.kotlin.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.kotlinKapt.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyKtlint.get().pluginId)
        plugin(rootProject.tonyLibs.plugins.tonyDepConfigurations.get().pluginId)
    }

    tasks.withType<Javadoc> {
        this.enabled = false
    }

    extensions.getByType<KotlinJvmProjectExtension>().apply {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
        }
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(javaVersion)
            languageVersion = KotlinVersion.fromVersion(kotlinVersion.substring(0..2))
            apiVersion= KotlinVersion.fromVersion(kotlinVersion.substring(0..2))
            jvmDefault = JvmDefaultMode.NO_COMPATIBILITY

            verbose = true
            progressiveMode = true
            extraWarnings = true
            // use kotlinc -X get more info.
            freeCompilerArgs.addAll(
                "-Xconsistent-data-class-copy-visibility",
                "-Xno-param-assertions",
                "-Xno-call-assertions",

                "-Xlambdas=indy",
                "-Xsam-conversions=indy",
                "-Xstring-concat=indy-with-constants",

                "-Xreport-all-warnings",

                "-Xjsr305=strict",
                "-Xenhance-type-parameter-types-to-def-not-null",

                "-Xjspecify-annotations=strict",
                "-Xtype-enhancement-improvements-strict-mode",
                // "-Xuse-fast-jar-file-system",
            )
        }
    }

    dependencies {
        add("implementation", platform(templateProject("dependencies")))
        add("implementation", platform(rootProject.tonyLibs.springCloudAlibabaDenpendencies))
        add("kapt", rootProject.tonyLibs.springContextIndexer)
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
