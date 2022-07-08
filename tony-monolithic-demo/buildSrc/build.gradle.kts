import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

val javaVersion:String by project

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = javaVersion
        allWarningsAsErrors = true
        verbose = true
        freeCompilerArgs = listOf(
            "-Xjsr305=strict -Xlint:all -Werror -verbose -encoding=UTF8 -deprecation -version -progressive"
        )
    }
}

repositories {
    val privateGradleRepoUrl: String by project
    maven(url = privateGradleRepoUrl) {
        isAllowInsecureProtocol = true
    }
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("com.palantir.gradle.docker:gradle-docker:0.34.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.1")
}
