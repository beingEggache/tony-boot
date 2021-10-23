dependencies {
    implementation(platform(rootProject))
    api(projects.tonyCore)
    api(Deps.Other.aliyunSdkOss)
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
