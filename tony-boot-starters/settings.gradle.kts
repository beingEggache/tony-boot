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
    includeBuild("build-script")
}

val kotlinVersion: String by settings
gradle.rootProject {
    buildscript {
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        }
    }
}
val projectPrefix: String by settings
rootProject.name = "$projectPrefix-dependencies"

include("$projectPrefix-aliyun-oss")
include("$projectPrefix-aliyun-sms")
include("$projectPrefix-web")
include("$projectPrefix-core")
include("$projectPrefix-redis")
include("$projectPrefix-alipay")
include("$projectPrefix-wechat")
include("$projectPrefix-jwt")
include("$projectPrefix-web-auth")
include("$projectPrefix-feign")
include("$projectPrefix-xxl-job")
include("$projectPrefix-mybatis-plus")
include("$projectPrefix-captcha")
include("$projectPrefix-snowflake-id")
include("$projectPrefix-knife4j-api")
include("$projectPrefix-web-crypto")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

