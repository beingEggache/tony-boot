apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.Tony.templateCore) { isChanging = true }
    implementation(Deps.Other.mysql)
    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)
    implementation(Deps.Tony.templateCache) { isChanging = true }
    api(project(":tony-dto"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
