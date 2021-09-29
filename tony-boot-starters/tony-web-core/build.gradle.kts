apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(Deps.SpringBoot.starterWeb){
        val list = Deps.SpringBoot.starterTomcat.split(":")
        exclude(list[0],list[1])
    }
    implementation(Deps.SpringBoot.starterUndertow)
    api(Deps.SpringBoot.starterValidation)
}
