apply(plugin = "kotlin-spring")
dependencies {

    //while execute gradle task, use -Pprofile=prod
    implementation(projects.tonyWebCore)
    if (project.extra["profile"] != "prod") {
        implementation(projects.tonyKnife4j)
    }
    implementation("net.sourceforge.tess4j:tess4j:4.5.4")
    implementation("commons-fileupload:commons-fileupload:1.4")
}
