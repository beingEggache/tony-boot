apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    implementation(projects.tonyKnife4jApi)
    implementation(Deps.Other.knife4j)
}
