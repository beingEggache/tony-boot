apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    apiOf(
        project(":tony-http"),
        Deps.Other.xstream
    )
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
