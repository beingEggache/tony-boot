dependencies {
    api(projects.tonyCore)

    implementation(Deps.SpringBoot.starterDataRedis)
    implementation(Deps.Other.commonsPool2)
    implementation(Deps.SpringBoot.starterAop)

    addTestDependencies()
    testApi(Deps.SpringBoot.starterDataRedis)
    testApi(Deps.SpringBoot.starterAop)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
