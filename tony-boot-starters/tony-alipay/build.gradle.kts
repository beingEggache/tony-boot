apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    api(project(":tony-core"))
    implementationOf(
        Deps.SpringBoot.springBoot,
        Deps.Other.alipaySdkJava
    )
}
