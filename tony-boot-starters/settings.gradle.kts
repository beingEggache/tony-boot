pluginManagement {
    repositories {
        val privateGradleRepoUrl: String by settings
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
    }
}

include("tony-aliyun-oss")
include("tony-aliyun-sms")
include("tony-web-core")
include("tony-core")
include("tony-http")
include("tony-cache")
include("tony-knife4j")
include("tony-knife4j-api")
include("tony-alipay")
include("tony-wechat")
include("tony-jwt")
include("tony-simple-auth")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

