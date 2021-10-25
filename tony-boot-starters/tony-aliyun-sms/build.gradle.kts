dependencies {
    api(projects.tonyCore)
    api(Deps.Aliyun.aliyunJavaSdkDysmsapi)
    implementation(Deps.Other.annotationApi)
    implementation(Deps.Aliyun.aliyunJavaSdkCore)
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
