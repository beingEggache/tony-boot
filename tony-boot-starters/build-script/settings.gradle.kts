pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/public")
        gradlePluginPortal()
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
        mavenCentral()
    }
}

rootProject.name = "build-script"
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
