dependencies {
    api(tonyLibs.kotlinStdlib)

    api(tonyLibs.springContext)
    api(tonyLibs.springBootStarterLogging)
    api(tonyLibs.logstashLogbackEncoder)
    api(tonyLibs.validationApi)

    api(tonyLibs.jacksonDatatypeJdk8)
    api(tonyLibs.jacksonDatatypeJsr310)
    api(tonyLibs.jacksonModuleKotlin)
    api(tonyLibs.jacksonModuleParameterNames)

    implementation(tonyLibs.bcprovJdk18On)
    compileOnly(tonyLibs.findbugsJsr305)
    compileOnly(tonyLibs.findbugsAnnotations)
}
