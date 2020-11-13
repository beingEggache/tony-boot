import com.tony.build.Deps
import com.tony.build.implementationOf

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

}
