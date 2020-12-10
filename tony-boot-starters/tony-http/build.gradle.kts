apply(plugin = "maven.publish")

dependencies {

    apiOf(
        project(":tony-core"),
        Deps.Other.httpclient
    )

}
