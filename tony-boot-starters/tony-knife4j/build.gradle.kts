apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {

    implementationOf(
        Deps.Tony.knife4jApi,
        Deps.Other.knife4j
    )
}
