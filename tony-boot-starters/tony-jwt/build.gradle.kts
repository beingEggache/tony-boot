dependencies {
    implementation(platform(rootProject))
    api(projects.tonyCore)
    api(Deps.Other.javaJwt)
    implementation(Deps.SpringBoot.autoconfigure)
    implementation(Deps.Other.annotationApi)
}
