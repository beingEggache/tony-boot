import com.tony.buildscript.Deps
import com.tony.buildscript.getProfile

apply(plugin = "kotlin-spring")
apply(plugin = "com.tony.build.docker")
dependencies {
    val profile = getProfile()
    //while execute gradle task, use -Dprofile=prod
    if (profile == "qa") {
        implementation(Deps.Template.templateKnife4j)
        implementation(Deps.Knife4j.openapi3Ui)
    }
    if (profile == "dev") {
        implementation(Deps.Template.templateKnife4j)
        implementation(Deps.Knife4j.openapi3Ui)
//        implementation(Deps.SpringBoot.devtools)
    }

    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Deps.Template.templateWeb)

    implementation(project(":tony-service"))
}
