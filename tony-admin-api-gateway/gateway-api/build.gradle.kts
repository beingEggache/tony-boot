apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.5")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    // gateway 限流用
    implementation("org.apache.commons:commons-pool2:2.11.1")
    implementation(Deps.SpringBoot.starterDataRedisReactive)

    implementation(Deps.Template.templateCore)
    implementation(Deps.Template.templateJwt)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
