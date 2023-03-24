pluginManagement {
    val privateGradleRepoUrl: String by settings
    repositories {
        mavenCentral()
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
    }
}

rootProject.name = "build-script"
