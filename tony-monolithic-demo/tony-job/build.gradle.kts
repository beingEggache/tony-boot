apply(plugin = "kotlin-spring")
dependencies {
    api(project(":tony-service"))
    implementation(tonyLibs.powerjobWorkerSpringBootStarter)
}
