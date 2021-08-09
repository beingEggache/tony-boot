apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")
dependencies {

    val profile = getProfiles()
    //while execute gradle task, use -Pprofile=prod
    if (profile == "qa") {
        implementation(Deps.Tony.tonyKnife4jApi)
    }
    if (profile == "dev") {
        implementation(Deps.Tony.tonyKnife4j)
        implementation(Deps.SpringBoot.devtools)
    }
    implementation(Deps.Tony.tonySimpleAuth) { isChanging = true }
    implementation(project(":tony-service"))
    addTestDependencies()
//    implementation(Deps.SpringBoot.starterActuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
