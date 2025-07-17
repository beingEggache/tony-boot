import tony.gradle.plugin.Build.Companion.profile
import tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
configurations.all {
    exclude(group= "com.alibaba.nacos", module = "logback-adapter")
    exclude(group= "com.alibaba.nacos", module = "nacos-log4j2-adapter")
}
dependencies {
    //while execute gradle task, use -Pprofile=prod
    if (profile() in setOf("qa", "dev")) {
        implementation(templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
        implementation(tonyLibs.swaggerV3AnnotaionJakarta)
        implementation(tonyLibs.springdocOpenapiKotlin)
    } else {
        compileOnly(tonyLibs.swaggerV3AnnotaionJakarta)
    }

    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(templateProject("web")) { isChanging = true }

    implementation(project(":tony-service"))
}
