pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
        mavenCentral()
    }
}

gradle.rootProject {
    buildscript {
        dependencies {
            classpath("com.tony:build-script:0.1-SNAPSHOT")
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
        mavenCentral()
    }
    versionCatalogs {
        create("tonyLibs") {
            from("com.tony:tony-dependencies-catalog:0.1-SNAPSHOT")
        }
    }
}

include("tony-api")
include("tony-service")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
