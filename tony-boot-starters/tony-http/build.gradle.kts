apply {
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {

    apiOf(
        project(":tony-core"),
        Deps.Other.httpclient
    )

}
