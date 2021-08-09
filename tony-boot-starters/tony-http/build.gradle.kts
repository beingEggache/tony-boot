apply {
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.httpclient)
}
