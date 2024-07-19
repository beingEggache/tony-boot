import com.tony.gradle.plugin.Build

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
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

    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Build.templateProject("web")) { isChanging = true }

    implementation(project(":tony-service"))
}
