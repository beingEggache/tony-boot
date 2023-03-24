import com.tony.buildscript.Deps
apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation(Deps.Other.caffeine)
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    // gateway 限流用
    implementation(Deps.Other.commonsPool2)
    implementation(Deps.SpringData.starterRedisReactive)

    implementation(Deps.Template.templateCore)
    implementation(Deps.Template.templateJwt)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
