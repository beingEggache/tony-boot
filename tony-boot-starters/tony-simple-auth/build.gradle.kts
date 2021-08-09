apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(projects.tonyJwt)
    api(projects.tonyWebCore)
}
