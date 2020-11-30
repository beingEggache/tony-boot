pluginManagement {
    repositories {
        val aliyunMavenRepoUrl: String by settings
        val privateMavenRepoUrl: String by settings
        gradlePluginPortal()
        maven(url = aliyunMavenRepoUrl)
        maven(url = privateMavenRepoUrl)
        mavenCentral()
        jcenter {
            content {
                // just allow to include kotlinx projects
                // detekt needs 'kotlinx-html' for the html report
                includeGroup( "org.jetbrains.kotlinx")
            }
        }
        mavenLocal()
    }
}

include("tony-aliyun-oss")
include("tony-aliyun-sms")
include("tony-web-core")
include("tony-core")
include("tony-http")
include("tony-cache")
include("tony-swagger")
include("tony-alipay")
include("tony-wechat")
