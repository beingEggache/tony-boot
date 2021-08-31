apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.Tony.tonyCore) { isChanging = true }
    implementation(Deps.Other.postgresql)
    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)
    implementation(Deps.Tony.tonyCache) { isChanging = true }
    api(project(":tony-dto"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
