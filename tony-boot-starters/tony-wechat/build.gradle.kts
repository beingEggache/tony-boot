dependencies {
    implementation(platform(Deps.SpringCloudDeps.springCloudDependencies))
    api(Deps.Other.xstream)
    implementation(Deps.SpringBoot.springBoot)
    api(projects.tonyFeign)
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
