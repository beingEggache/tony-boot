plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
    implementation("com.palantir.gradle.docker:gradle-docker:0.25.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.4.0")
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.10")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.10")
        }
    }
}
