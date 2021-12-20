dependencies {
    implementation(platform(Deps.SpringCloudDeps.springCloudDependencies))
    api(projects.tonyCore)
    api(Deps.OpenFeign.openFeignOkhttp)
    api(Deps.OpenFeign.openFeignJackson)
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation(Deps.Other.caffeine)
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
