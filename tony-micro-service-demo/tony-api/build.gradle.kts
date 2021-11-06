apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")

dependencies {

    val profile = getProfiles()
    //while execute gradle task, use -Dprofile=prod
    if (profile == "qa") {
        implementation(Deps.Tony.tonyKnife4jApi)
    }
    if (profile == "dev") {
        implementation(Deps.Tony.tonyKnife4j)
        implementation(Deps.SpringBoot.devtools)
    }

    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Deps.Tony.tonyWeb)

    implementation(project(":tony-service"))
}
