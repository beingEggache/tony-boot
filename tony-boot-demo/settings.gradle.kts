pluginManagement {
    repositories {
        val aliyunMavenRepoUrl: String by settings
        val privateMavenRepoUrl: String by settings
        gradlePluginPortal()
        maven(url = aliyunMavenRepoUrl) { isAllowInsecureProtocol = true }
        maven(url = privateMavenRepoUrl) { isAllowInsecureProtocol = true }
        mavenCentral()
        mavenLocal()
    }
}

include("tony-api")
include("tony-service")
include("tony-dto")
