plugins {
    `kotlin-dsl`
}

repositories {
    val privateGradleRepoUrl: String by project
    maven(url = privateGradleRepoUrl) {
        isAllowInsecureProtocol = true
    }
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("com.palantir.gradle.docker:gradle-docker:0.25.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.5.5")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
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
