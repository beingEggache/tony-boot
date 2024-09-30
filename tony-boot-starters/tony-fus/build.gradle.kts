dependencies {
    api(projects.tonyCore)
    implementation(projects.tonyMybatisPlus)
    testImplementation(tonyLibs.mysql)

//    compileOnly(tonyLibs.findbugsJsr305)
//    compileOnly(tonyLibs.findbugsAnnotations)

    api(tonyLibs.springBootStarterAop)
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
}
