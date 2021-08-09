apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.aliyunSdkOss)
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
