apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    // gateway 限流用
    implementation("org.apache.commons:commons-pool2:2.10.0")
    implementation(Deps.SpringBoot.starterDataRedisReactive)

    implementation(Deps.Tony.tonyCore)
    implementation(Deps.Tony.tonyJwt)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
