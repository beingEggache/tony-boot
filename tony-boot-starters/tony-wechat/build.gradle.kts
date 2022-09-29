dependencies {
    api(projects.tonyFeign)

    api(Deps.Other.xstream)
    implementation(Deps.Other.commonsCodec)

    implementation(platform(Deps.SpringCloudDeps.springCloudDependencies))
    implementation(Deps.SpringBoot.springBoot)

    addTestDependencies()
    testImplementation(projects.tonyCache)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
