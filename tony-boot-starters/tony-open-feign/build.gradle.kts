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
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer:3.0.3")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
