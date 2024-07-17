import com.tony.gradle.plugin.Build

apply(plugin = "kotlin-spring")
apply(plugin = "com.tony.gradle.plugin.docker")
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation(tonyLibs.caffeine)
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    // gateway 限流用
    implementation(tonyLibs.commonsPool2)
    implementation(tonyLibs.springBootStarterDataRedisReactive)

    implementation(Build.templateProject("core")) { isChanging = true }
    implementation(Build.templateProject("jwt")) { isChanging = true }
}
