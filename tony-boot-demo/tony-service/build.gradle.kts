apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.TonyBoot.tonyCore) { isChanging = true }
    implementation(Deps.Other.postgresql)
    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisStarter)
    implementation(Deps.Other.mybatisTypehandlersJsr310)
    implementation(Deps.TonyBoot.tonyCache) { isChanging = true }
    api(project(":tony-dto"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
