apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.Tony.tonyCore) { isChanging = true }
    implementation(Deps.Other.mysql)
    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)
    implementation(Deps.Tony.tonyCache) { isChanging = true }
    implementation(Deps.Tony.tonyOpenFeign) { isChanging = true }
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
