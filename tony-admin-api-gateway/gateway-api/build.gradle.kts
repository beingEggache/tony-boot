import tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
configurations.all {
    exclude(group= "com.alibaba.nacos", module = "logback-adapter")
    exclude(group= "com.alibaba.nacos", module = "nacos-log4j2-adapter")
}
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation(tonyLibs.caffeine)
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    // gateway 限流用
    implementation(tonyLibs.commonsPool2)
    implementation(tonyLibs.springBootStarterDataRedisReactive)

    implementation("org.json:json:20250517")

    implementation(templateProject("core")) { isChanging = true }
    implementation(templateProject("jwt")) { isChanging = true }
}
