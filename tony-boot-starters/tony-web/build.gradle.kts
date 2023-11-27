dependencies {
    api(projects.tonyCore)
    api(projects.tonyInterfaces)
    api(tonyLibs.springBootStarterWeb) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api(tonyLibs.springBootStarterUndertow)
    implementation(tonyLibs.springBootStarterValidation)

    testImplementation(projects.tonyKnife4jApi)
    testImplementation(tonyLibs.knife4jOpenapi3Ui)
}
