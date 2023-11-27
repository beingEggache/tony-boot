import com.tony.gradle.plugin.Build

dependencies {
    //while execute gradle task, use -Pprofile=prod
    api(tonyLibs.validationApi)
    api(tonyLibs.swaggerV3Annotaion)
    implementation(Build.templateProject("core")){ isChanging = true }
    implementation(tonyLibs.springWeb)
    implementation(tonyLibs.springContext)
}
