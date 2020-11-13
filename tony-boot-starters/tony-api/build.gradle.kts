import com.tony.build.Deps

version = "unspecified"

apply(plugin = "kotlin-spring")
dependencies {

    //while execute gradle task, use -Pprofile=prod
    implementation(project(":tony-web-core"))
    implementation(Deps.Other.swaggerAnnotations)
    implementation("io.springfox:springfox-swagger-ui:${com.tony.build.Version.springfoxSwagger2Version}")

    if (project.extra["profile"] != "prod") {
        implementation(project(":tony-swagger"))
    }
}
