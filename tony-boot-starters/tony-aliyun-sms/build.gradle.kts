dependencies {
    api(projects.tonyCore)

    api(Deps.Aliyun.aliyunJavaSdkDysmsapi)

    implementation(Deps.SpringBoot.springBoot)

    implementation(Deps.Other.annotationApi)
    implementation(Deps.Aliyun.aliyunJavaSdkCore)

    addTestDependencies()
}
