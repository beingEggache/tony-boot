import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
import com.tony.buildscript.getProfile

apply(plugin = "com.tony.build.docker")
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
    addTestDependencies()
//    implementation(Deps.SpringBoot.starterActuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
