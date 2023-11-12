dependencies {
    api(projects.tonyCore)
    api(tonyLibs.mybatisPlusBootStarter3)
    implementation(projects.tonyInterfaces)

    implementation(tonyLibs.hikariCP)
    implementation(tonyLibs.mybatisTypehandlersJsr310)

    testImplementation(tonyLibs.mysql)
    testImplementation(projects.tonySnowflakeId)
}
