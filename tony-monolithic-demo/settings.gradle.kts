pluginManagement {
    repositories {
        mavenCentral()
        val privateGradleRepoUrl: String by settings
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
        mavenLocal()
    }
}

gradle.rootProject {
    val kotlinVersion: String by settings
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
