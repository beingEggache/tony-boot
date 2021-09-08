apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    implementation(Deps.OpenFeign.starterOpenFeign)
    implementation(Deps.OpenFeign.openFeignOkhttp)
    implementation(Deps.OpenFeign.openFeignJackson)
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
