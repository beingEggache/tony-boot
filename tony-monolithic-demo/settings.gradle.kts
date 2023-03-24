pluginManagement {
    val privateGradleRepoUrl: String by settings
    repositories {
        mavenLocal()
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

val kotlinVersion: String by settings
gradle.rootProject {
    buildscript {
        dependencies {
            classpath("com.tony:build-script:0.1-SNAPSHOT")
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        }
    }
}

include("tony-api")
include("tony-service")
include("tony-dto")
