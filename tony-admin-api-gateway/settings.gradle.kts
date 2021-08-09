pluginManagement {
    repositories {
        val privateGradleRepoUrl: String by settings
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
    }
}

include("gateway-api")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
