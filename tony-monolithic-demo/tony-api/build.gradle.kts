import com.tony.gradle.Deps
import com.tony.gradle.getProfile

apply(plugin = "kotlin-spring")
apply(plugin = "com.tony.gradle.plugin.docker")
dependencies {

    val profile = getProfile()
    //while execute gradle task, use -Pprofile=prod
    if (profile == "qa") {
        implementation(Deps.Template.templateKnife4j)
        implementation(Deps.Knife4j.openapi3Ui)
    }
    if (profile == "dev") {
        implementation(Deps.Template.templateKnife4j)
        implementation(Deps.Knife4j.openapi3Ui)
//        implementation(Deps.SpringBoot.devtools)
    }
    implementation(Deps.Template.templateWeb) { isChanging = true }
    implementation(Deps.Template.templateWebAuth) { isChanging = true }
    implementation(project(":tony-service"))
//    implementation(Deps.SpringBoot.starterActuator)
}
