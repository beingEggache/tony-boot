import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    mavenCentral()
    mavenLocal()
}

tasks.withType<KotlinCompile>().configureEach {
    val isTest = this.name.contains("test", ignoreCase = true)
    kotlinOptions {
        jvmTarget = "11"
        allWarningsAsErrors = !isTest
        verbose = true
        freeCompilerArgs = listOf(
            "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation"
        )
    }
}
