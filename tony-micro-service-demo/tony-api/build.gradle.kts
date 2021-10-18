apply {
    plugin("kotlin-spring")
}

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

    implementation(platform("com.alibaba.cloud:spring-cloud-alibaba-dependencies:2021.1"))
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2020.0.4"))
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation(Deps.Tony.tonyWebCore)

    implementation(project(":tony-service"))
}
