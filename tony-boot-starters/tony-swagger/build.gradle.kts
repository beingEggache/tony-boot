apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {

    implementationOf(
        Deps.SpringBoot.springBoot,
        Deps.Spring.web,
        Deps.Other.springfoxSwagger2,
        Deps.Other.knife4j
    )
}
