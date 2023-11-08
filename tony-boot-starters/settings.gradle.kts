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
    includeBuild("build-script")
}

dependencyResolutionManagement {
    defaultLibrariesExtensionName = "tonyLibs"
}

val projectPrefix: String by settings
rootProject.name = "$projectPrefix-boot-starters"
include("$projectPrefix-dependencies")
include("$projectPrefix-dependencies-catalog")
include("$projectPrefix-aliyun-oss")
include("$projectPrefix-aliyun-sms")
include("$projectPrefix-interfaces")
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
include("$projectPrefix-flow")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

