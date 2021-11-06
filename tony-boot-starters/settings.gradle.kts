pluginManagement {
    repositories {
        mavenCentral()
        val privateGradleRepoUrl: String by settings
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
    }
}

rootProject.name = "tony-dependencies"

include("tony-aliyun-oss")
include("tony-aliyun-sms")
include("tony-web")
include("tony-core")
include("tony-cache")
include("tony-knife4j")
include("tony-knife4j-api")
include("tony-alipay")
include("tony-wechat")
include("tony-jwt")
include("tony-web-auth")
include("tony-open-feign")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
