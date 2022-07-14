dependencies {
    api(projects.tonyCore)

    implementation(Deps.SpringBoot.springBoot)

    api(Deps.Aliyun.aliyunSdkOss)
    api(Deps.Aliyun.aliyunJavaSdkCore)

    addTestDependencies()
}
