dependencies {

    api(projects.tonyCore)

    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    api(Deps.OpenFeign.openFeignOkhttp)
    api(Deps.OpenFeign.openFeignJackson)

    implementation(platform(Deps.SpringCloudDeps.springCloudDependencies))
    implementation(Deps.Other.caffeine)

    addTestDependencies()
    testImplementation(projects.tonyWebAuth)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
