pluginManagement {
    val privateGradleRepoUrl: String by settings
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven(url = privateGradleRepoUrl) {
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }
}

rootProject.name = "build-script"
