dependencies {
    implementation(platform(rootProject))
    api(projects.tonyCore)
    implementation(Deps.SpringBoot.starterDataRedis)
    implementation(Deps.SpringBoot.starterAop)

    addTestDependencies()
    testApi(Deps.SpringBoot.starterDataRedis)
    testApi(Deps.SpringBoot.starterAop)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
