apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    implementation(Deps.SpringBoot.springBoot)
    implementation(Deps.Spring.web)
    implementation(Deps.Other.springfoxSwagger2)
    implementation(Deps.Other.knife4jApi)
}
