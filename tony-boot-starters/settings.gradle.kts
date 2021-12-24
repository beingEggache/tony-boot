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
val projectPrefix: String by settings
rootProject.name = "$projectPrefix-dependencies"

include("$projectPrefix-aliyun-oss")
include("$projectPrefix-aliyun-sms")
include("$projectPrefix-web")
include("$projectPrefix-core")
include("$projectPrefix-cache")
include("$projectPrefix-alipay")
include("$projectPrefix-wechat")
include("$projectPrefix-jwt")
include("$projectPrefix-web-auth")
include("$projectPrefix-feign")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
