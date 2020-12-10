apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    apiOf(
        project(":tony-core"),
        Deps.Other.aliyunSdkOss
    )
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
