import org.gradle.kotlin.dsl.support.serviceOf

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
            from(
                buildscript.configurations.create("catalog") {
                    dependencies.add(
                        buildscript.dependencies
                            .create("com.tony:tony-dependencies-catalog:0.1-SNAPSHOT")
                    )
                    attributes {
                        val objects = gradle.serviceOf<ObjectFactory>()

                        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.REGULAR_PLATFORM))
                        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.VERSION_CATALOG))
                    }
                }
            )
        }
    }
}

include("tony-api")
include("tony-service")
include("tony-job")
include("tony-dto")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
