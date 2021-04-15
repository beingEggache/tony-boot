apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.javaJwt)
    api(Deps.SpringBoot.starterWeb)
    api(Deps.SpringBoot.starterValidation)
}
