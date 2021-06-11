pluginManagement {
    repositories {
        val aliyunMavenRepoUrl: String by settings
        val aliyunGradleRepoUrl: String by settings
        val privateMavenRepoUrl: String by settings
        maven(url = aliyunGradleRepoUrl)
        gradlePluginPortal()
        maven(url = aliyunMavenRepoUrl)
        maven(url = privateMavenRepoUrl)
        mavenCentral()
        mavenLocal()
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
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
