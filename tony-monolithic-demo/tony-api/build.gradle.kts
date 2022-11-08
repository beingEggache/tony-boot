import com.tony.buildscript.Deps
import com.tony.buildscript.getProfile
import com.tony.buildscript.addTestDependencies
apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")
dependencies {

    val profile = getProfile()
    //while execute gradle task, use -Pprofile=prod
    if (profile == "qa") {
        implementation(Deps.Other.springdocCommon)
        implementation(Deps.Other.springdocKotlin)
    }
    if (profile == "dev") {
        implementation(Deps.Other.springdocKotlin)
        implementation(Deps.Other.springdocUi)
        implementation(Deps.SpringBoot.devtools)
    }
    implementation(Deps.Template.templateWeb) { isChanging = true }
    implementation(Deps.Template.templateWebAuth) { isChanging = true }
    implementation(Deps.Template.templateFeign) { isChanging = true }
    implementation(project(":tony-service"))
//    implementation(project(":tony-amqp"))
    addTestDependencies()
//    implementation(Deps.SpringBoot.starterActuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
