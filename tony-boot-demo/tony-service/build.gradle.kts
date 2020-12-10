apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.TonyBoot.core) { isChanging = true }
    implementationOf(
        Deps.Other.postgresql,
        Deps.Other.HikariCP,
        Deps.Other.mybatisStarter,
        Deps.Other.mybatisTypehandlersJsr310
    )
    implementation(Deps.TonyBoot.cache) { isChanging = true }
    api(project(":tony-pojo"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
