apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {

    apiOf(
        project(":tony-core"),
        Deps.Other.aliyunSdkOss
    )
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
