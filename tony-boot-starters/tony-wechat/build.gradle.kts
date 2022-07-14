dependencies {
    api(projects.tonyFeign)

    api(Deps.Other.xstream)

    implementation(platform(Deps.SpringCloudDeps.springCloudDependencies))
    implementation(Deps.SpringBoot.springBoot)

    addTestDependencies()
    testImplementation(projects.tonyCache)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
