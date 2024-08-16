import com.tony.gradle.plugin.Build.Companion.templateProject

dependencies {
    //while execute gradle task, use -Pprofile=prod
    api(tonyLibs.validationApi)
    api(tonyLibs.swaggerV3AnnotaionJakarta)
    implementation(templateProject("core")){ isChanging = true }
    implementation(templateProject("interfaces")){ isChanging = true }
    implementation(tonyLibs.springWeb)
    implementation(tonyLibs.springContext)
    api("com.alibaba:easyexcel:4.0.1")

}
