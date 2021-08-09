apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(projects.tonyCore)
    implementation(Deps.SpringBoot.springBoot)
    implementation(Deps.Other.alipaySdkJava)
}
