dependencies {
    api(projects.tonyCore)
    api(projects.tonyInterfaces)
    api("org.springframework.cloud:spring-cloud-starter-openfeign")

    api(tonyLibs.openFeignOkhttp)
    api(tonyLibs.openFeignJackson)
    testImplementation(projects.tonyWebAuth)
}
