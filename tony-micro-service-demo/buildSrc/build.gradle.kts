import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        allWarningsAsErrors = true
        verbose = true
        freeCompilerArgs = listOf(
            "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation -version -progressive"
        )
    }
}

dependencies {
    implementation("com.palantir.gradle.docker:gradle-docker:0.29.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.5.6")
}
