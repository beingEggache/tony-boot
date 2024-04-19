import com.tony.gradle.plugin.Build

dependencies {
    //while execute gradle task, use -Pprofile=prod
    api(tonyLibs.validationApi)
    api(tonyLibs.swaggerV3Annotaion)
    implementation(Build.templateProject("core")){ isChanging = true }
    implementation(Build.templateProject("interfaces")){ isChanging = true }
    implementation(tonyLibs.springWeb)
    implementation(tonyLibs.springContext)
    api("com.alibaba:easyexcel:3.3.4")

}
