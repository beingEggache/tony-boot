dependencies {
    implementation(platform(rootProject))
    api(projects.tonyCore)
    api(Deps.Other.aliyunJavaSdkDysmsapi)
    implementation(Deps.Other.annotationApi)
    implementation(Deps.Other.aliyunJavaSdkCore)
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
