plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.palantir.gradle.docker:gradle-docker:0.25.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.5.1")
}
