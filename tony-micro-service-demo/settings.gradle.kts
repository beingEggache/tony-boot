buildscript {
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
    dependencies {
        classpath("com.tony:build-script:0.1-SNAPSHOT")
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
    }
    versionCatalogs {
        register("tonyLibs") {
            from("com.tony:tony-dependencies-catalog:0.1-SNAPSHOT")
        }
    }
}

include("tony-api")
include("tony-service")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
