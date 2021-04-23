plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    val isTest = this.name.contains("test", ignoreCase = true)
    kotlinOptions {
        languageVersion = "1.4"
        jvmTarget = "11"
        allWarningsAsErrors = !isTest
        verbose = true
        freeCompilerArgs = listOf(
            "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation"
        )
    }
}
