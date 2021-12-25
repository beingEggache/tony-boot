apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")

dependencies {

    val profile = getProfile()
    //while execute gradle task, use -Dprofile=prod
    if (profile == "qa") {
        implementation(Deps.Other.springdocCommon)
        implementation(Deps.Other.springdocKotlin)
    }
    if (profile == "dev") {
        implementation(Deps.Other.springdocKotlin)
        implementation(Deps.Other.springdocUi)
        implementation(Deps.SpringBoot.devtools)
    }

    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Deps.Template.templateWeb)

    implementation(project(":tony-service"))
}
