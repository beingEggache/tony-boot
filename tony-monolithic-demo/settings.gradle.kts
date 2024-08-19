import com.tony.gradle.plugin.Build.Companion.templateProject

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
        val buildScript: String by settings
        classpath(buildScript)
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
            from(templateProject("dependencies-catalog"))
        }
    }
}

include("tony-api")
include("tony-service")
include("tony-job")
include("tony-dto")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
