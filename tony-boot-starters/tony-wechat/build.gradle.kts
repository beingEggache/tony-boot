apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(Deps.Other.xstream)
    implementation(Deps.SpringBoot.springBoot)
    api(projects.tonyOpenFeign)
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
