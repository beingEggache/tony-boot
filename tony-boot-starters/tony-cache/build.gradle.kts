apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(project(":tony-core"))
    implementationOf(
        Deps.SpringBoot.starterDataRedis,
        Deps.SpringBoot.starterAop
    )
}
