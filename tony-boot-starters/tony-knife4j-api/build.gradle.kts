dependencies {
    api(projects.tonyCore)
    api(tonyLibs.knife4jCore)
    api(tonyLibs.swaggerV3AnnotaionJakarta)
    api(tonyLibs.springdocStarterCommon)

    implementation(tonyLibs.knife4jOpenapi3JakartaSpringBootStarter)
    implementation(tonyLibs.springBootAutoconfigure)

    testImplementation(projects.tonyWeb)
    testImplementation(tonyLibs.knife4jOpenapi3Ui)
    testImplementation("cn.idev.excel:fastexcel:1.2.0")
}
