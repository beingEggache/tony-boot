pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
        mavenCentral()
    }
}

rootProject.name = "build-script"
