apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    implementation(Deps.Tony.knife4jApi)
    implementation(Deps.Other.knife4j)
}
