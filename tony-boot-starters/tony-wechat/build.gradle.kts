dependencies {
    api(projects.tonyFeign)
    api(tonyLibs.xstream)

    implementation(tonyLibs.commonsCodec)
    implementation(tonyLibs.springBoot)

    testImplementation(projects.tonyRedis)
}
