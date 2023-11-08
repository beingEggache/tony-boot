dependencies {
    api(projects.tonyCore)
    api(tonyLibs.mybatisPlusBootStarter3)

    implementation(tonyLibs.hikariCP)
    implementation(tonyLibs.mybatisTypehandlersJsr310)

    testImplementation(tonyLibs.mysql)
}
