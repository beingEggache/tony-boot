apply(plugin = "kotlin-spring")
dependencies {

    //while execute gradle task, use -Pprofile=prod
    implementation(projects.tonyWebCore)
    if (project.extra["profile"] != "prod") {
        implementation(projects.tonyKnife4j)
    }
}
