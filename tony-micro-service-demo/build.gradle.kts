import com.tony.gradle.plugin.Build
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
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
            jvmTarget.set(JvmTarget.fromTarget(javaVersion))
            languageVersion.set(KotlinVersion.fromVersion(kotlinVersion.substring(0..2)))
            apiVersion.set(KotlinVersion.fromVersion(kotlinVersion.substring(0..2)))
            verbose.set(true)
            progressiveMode.set(true)
            // use kotlinc -X get more info.
            freeCompilerArgs.addAll(
                "-Xjsr305=strict",
                "-Xjvm-default=all",
                "-Xlambdas=indy",
                "-Xsam-conversions=indy",
                "-Xjspecify-annotations=strict",
                "-Xtype-enhancement-improvements-strict-mode",
                "-Xenhance-type-parameter-types-to-def-not-null",
                "-Xextended-compiler-checks",
                "-java-parameters",
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
