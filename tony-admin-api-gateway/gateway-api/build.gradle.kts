import tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation(tonyLibs.caffeine)
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-discovery")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-config")

    implementation("com.tencent.cloud:spring-cloud-starter-tencent-gateway-plugin")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-metadata-transfer")

    // gateway 限流用
    implementation(tonyLibs.commonsPool2)
    implementation(tonyLibs.springBootStarterDataRedisReactive)

    implementation("org.json:json:20250517")

    implementation(templateProject("core")) { isChanging = true }
    implementation(templateProject("jwt")) { isChanging = true }
}
