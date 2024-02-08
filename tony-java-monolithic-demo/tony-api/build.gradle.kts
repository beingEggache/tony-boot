import com.tony.gradle.plugin.Build

apply(plugin = "com.tony.gradle.plugin.docker")
dependencies {
    val profile = Build.getProfile()

    //while execute gradle task, use -Pprofile=prod
    if (profile == "qa") {
        implementation(Build.templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }
    if (profile == "dev") {
        implementation(Build.templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }
    implementation(Build.templateProject("web")) { isChanging = true }
    implementation(Build.templateProject("web-auth")) { isChanging = true }
    testImplementation(Build.templateProject("fus")) { isChanging = true }
    implementation(project(":tony-service"))
}
