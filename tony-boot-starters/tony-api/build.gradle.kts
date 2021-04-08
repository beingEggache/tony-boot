apply(plugin = "kotlin-spring")
dependencies {

    //while execute gradle task, use -Pprofile=prod
    implementation(project(":tony-web-core"))
    if (project.extra["profile"] != "prod") {
        implementation(project(":tony-knife4j"))
    }
}
