apply(plugin = "kotlin-spring")
apply(plugin = "docker.publish")
dependencies {

    //while execute gradle task, use -Pprofile=prod
    if (project.extra["profile"] != "prod") {
        implementation(Deps.TonyBoot.swagger)
    }
    if (project.extra["profile"] == "dev") {
        implementation(Deps.SpringBoot.devtools)
    }
    implementation(Deps.TonyBoot.webCore) { isChanging = true }
    implementationProjects(
        ":tony-service"
    )
    addTestDependencies()
//    implementation(Deps.SpringBoot.starterActuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
