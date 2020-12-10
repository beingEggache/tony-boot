apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    api(project(":tony-core"))
    apiOf(
        Deps.Other.javaJwt,
        Deps.SpringBoot.starterWeb,
        Deps.SpringBoot.starterValidation
    )
}
