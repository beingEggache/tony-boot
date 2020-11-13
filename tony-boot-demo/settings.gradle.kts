pluginManagement {
    repositories {
        val aliyunMavenRepoUrl: String by settings
        val privateMavenRepoUrl: String by settings
        gradlePluginPortal()
        maven(url = aliyunMavenRepoUrl) { isAllowInsecureProtocol = true }
        maven(url = privateMavenRepoUrl) { isAllowInsecureProtocol = true }
        mavenCentral()
        jcenter {
            content {
                // just allow to include kotlinx projects
                // detekt needs 'kotlinx-html' for the html report
                includeGroup("org.jetbrains.kotlinx")
            }
        }
        mavenLocal()
    }
}

include("tony-api")
include("tony-service")
include("tony-pojo")
