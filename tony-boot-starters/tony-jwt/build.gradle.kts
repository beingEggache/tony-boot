apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.javaJwt)
    implementation(Deps.SpringBoot.autoconfigure)
    implementation(Deps.Other.annotationApi)
}
