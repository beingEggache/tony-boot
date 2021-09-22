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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include("tony-api")
include("tony-service")
