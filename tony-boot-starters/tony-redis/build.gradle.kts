dependencies {
    api(projects.tonyCore)
    api(projects.tonyInterfaces)
    implementation(tonyLibs.springBootStarterDataRedis)
    implementation(tonyLibs.springBootStarterAop)
    implementation(tonyLibs.springBootStarterJson)
//    compileOnly("io.protostuff:protostuff-core:1.8.0")
//    compileOnly("io.protostuff:protostuff-runtime:1.8.0")

    testApi(tonyLibs.springBootStarterDataRedis)
    testApi(tonyLibs.springBootStarterAop)
//    testImplementation("io.protostuff:protostuff-core:1.8.0")
//    testImplementation("io.protostuff:protostuff-runtime:1.8.0")
//    testImplementation(tonyLibs.furyCore)
    testImplementation("com.github.codemonstur:embedded-redis:1.4.3")
}
