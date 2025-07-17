import tony.gradle.plugin.Build.Companion.profile
import tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
dependencies {
    //while execute gradle task, use -Pprofile=prod
    if (profile() in setOf("qa", "dev")) {
        implementation(templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
        implementation(tonyLibs.swaggerV3AnnotaionJakarta)
    } else {
        compileOnly(tonyLibs.swaggerV3AnnotaionJakarta)
    }
    implementation(templateProject("web")) { isChanging = true }
    implementation(templateProject("web-auth")) { isChanging = true }
    implementation(project(":tony-service"))
}
