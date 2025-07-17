import tony.gradle.plugin.Build.Companion.profile
import tony.gradle.plugin.Build.Companion.templateProject

dependencies {
    //while execute gradle task, use -Pprofile=prod
    if (profile() in setOf("qa", "dev")) {
        api(tonyLibs.swaggerV3AnnotaionJakarta)
    } else {
        compileOnly(tonyLibs.swaggerV3AnnotaionJakarta)
    }
    api(tonyLibs.validationApi)
    implementation(templateProject("core")) { isChanging = true }
    implementation(templateProject("interfaces")) { isChanging = true }
    implementation(tonyLibs.springWeb)
    implementation(tonyLibs.springContext)
    api("cn.idev.excel:fastexcel:1.2.0")

}
