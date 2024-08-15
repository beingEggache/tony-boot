import com.tony.gradle.plugin.Build

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
dependencies {
    val profile = Build.getProfile()
    //while execute gradle task, use -Dprofile=prod
    if (profile in setOf("qa", "dev")) {
        implementation(Build.templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }

    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-discovery")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-config")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-ratelimit")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-circuitbreaker")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-router")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-metadata-transfer")

//    implementation("com.tencent.cloud:spring-cloud-tencent-lossless-plugin")
    implementation("com.tencent.cloud:spring-cloud-tencent-featureenv-plugin")
    implementation("com.tencent.cloud:spring-cloud-tencent-rpc-enhancement")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Build.templateProject("web")) { isChanging = true }

    implementation(project(":tony-service"))
}
