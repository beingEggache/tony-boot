apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(Deps.OpenFeign.openFeignOkhttp)
    api(Deps.OpenFeign.openFeignJackson)
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
