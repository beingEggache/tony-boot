import tony.gradle.plugin.Build.Companion.profile
import tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
dependencies {
    //while execute gradle task, use -Dprofile=prod
    if (profile() in setOf("qa", "dev")) {
        implementation(templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }

    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(templateProject("web")) { isChanging = true }

    implementation(project(":tony-service"))
}
