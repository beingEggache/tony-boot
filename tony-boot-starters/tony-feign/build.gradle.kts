dependencies {
    api(projects.tonyCore)
    api(projects.tonyInterfaces)
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    api(tonyLibs.openFeignOkhttp)
    api(tonyLibs.openFeignJackson)
    implementation(tonyLibs.caffeine)

    testImplementation(projects.tonyWebAuth)
}
