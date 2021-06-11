apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")
dependencies {

    val profile = getProfiles()
    //while execute gradle task, use -Pprofile=prod
    if (profile == "qa") {
        implementation(Deps.TonyBoot.tonyKnife4jApi)
    }
    if (profile == "dev") {
        implementation(Deps.TonyBoot.tonyKnife4j)
        implementation(Deps.SpringBoot.devtools)
    }
    implementation(Deps.TonyBoot.tonyWebCore) { isChanging = true }
    implementation(project(":tony-service"))
    addTestDependencies()
//    implementation(Deps.SpringBoot.starterActuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
