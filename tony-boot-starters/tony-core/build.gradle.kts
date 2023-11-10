dependencies {
    api(tonyLibs.kotlinStdlib)
    api(tonyLibs.kotlinReflect)
    api(tonyLibs.springContext)
    api(tonyLibs.springBootStarterLogging)
    api(tonyLibs.validationApi)
    api(tonyLibs.jacksonDatatypeJdk8)
    api(tonyLibs.jacksonDatatypeJsr310)
    api(tonyLibs.jacksonModuleKotlin)
    api(tonyLibs.jacksonModuleParameterNames)

    implementation(tonyLibs.bcprovJdk18On)
    compileOnly(tonyLibs.findbugsJsr305)
}
