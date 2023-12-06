dependencies {
    api(projects.tonyWeb)

    testImplementation(projects.tonyKnife4jApi)
    testImplementation(tonyLibs.knife4jOpenapi3Ui)
    testImplementation(projects.tonyFeign)
    testImplementation(projects.tonyInterfaces)
}
