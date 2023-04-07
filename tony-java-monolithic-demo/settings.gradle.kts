pluginManagement {
    repositories {
        mavenLocal()
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
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            classpath("com.tony:build-script:0.1-SNAPSHOT")
        }
    }
}

include("tony-api")
include("tony-service")
include("tony-dto")
