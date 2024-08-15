import com.tony.gradle.plugin.Build

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
dependencies {

    val profile = Build.getProfile()
    //while execute gradle task, use -Pprofile=prod
    if (profile in setOf("qa", "dev")) {
        implementation(Build.templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }
    implementation(Build.templateProject("web")) { isChanging = true }
    implementation(Build.templateProject("web-auth")) { isChanging = true }
    implementation(project(":tony-service"))
    implementation(project(":tony-job"))
//    implementation(Deps.SpringBoot.starterActuator)
}
