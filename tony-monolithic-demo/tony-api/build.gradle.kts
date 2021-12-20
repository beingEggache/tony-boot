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
    implementation(Deps.Tony.tonyWebAuth) { isChanging = true }
    implementation(Deps.Tony.tonyFeign)
    implementation(project(":tony-service"))
    addTestDependencies()
//    implementation(Deps.SpringBoot.starterActuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
