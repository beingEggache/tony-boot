pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        gradlePluginPortal()
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
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
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
