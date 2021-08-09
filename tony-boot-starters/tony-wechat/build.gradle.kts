apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyHttp)
    api(Deps.Other.xstream)
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
