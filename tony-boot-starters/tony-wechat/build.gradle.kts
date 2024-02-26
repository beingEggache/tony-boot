dependencies {
    api(projects.tonyFeign)
    api(tonyLibs.xstream)

    implementation(tonyLibs.springBoot)

    testImplementation(projects.tonyRedis)
}
