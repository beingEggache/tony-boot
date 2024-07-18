apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
dependencies {
    api(project(":tony-service"))
    implementation(tonyLibs.powerjobWorkerSpringBootStarter)
}
