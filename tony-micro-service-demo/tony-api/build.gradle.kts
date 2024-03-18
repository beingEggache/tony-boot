import com.tony.gradle.plugin.Build

apply(plugin = "kotlin-spring")
apply(plugin = "com.tony.gradle.plugin.docker")
dependencies {
    val profile = Build.getProfile()
    //while execute gradle task, use -Dprofile=prod
    if (profile == "qa") {
        implementation(Build.templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }
    if (profile == "dev") {
        implementation(Build.templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }

    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-discovery")
    implementation("com.tencent.cloud:spring-cloud-starter-tencent-polaris-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Build.templateProject("web")) { isChanging = true }

    implementation(project(":tony-service"))
}
