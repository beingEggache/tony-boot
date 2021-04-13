apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(Deps.SpringBoot.starterLogging)
    api(Deps.Other.aliyunJavaSdkDysmsapi)
    implementation(Deps.Other.annotationApi)
    implementation(Deps.Other.aliyunJavaSdkCore)
    implementation(Deps.SpringBoot.springBoot)

    addTestDependencies()
}
