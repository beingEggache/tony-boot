apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {
    apiOf(
        Deps.SpringBoot.starterLogging,
        Deps.Other.aliyunJavaSdkDysmsapi
    )
    implementationOf(
        Deps.Other.annotationApi,
        Deps.Other.aliyunJavaSdkCore,
        Deps.SpringBoot.springBoot
    )

    addTestDependencies()
}
