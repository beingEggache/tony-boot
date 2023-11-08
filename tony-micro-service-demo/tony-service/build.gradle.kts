import com.tony.gradle.Deps
apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.Template.templateCore) { isChanging = true }
    implementation(Deps.Other.mysql)
    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)
    implementation(Deps.Template.templateRedis) { isChanging = true }
    implementation(Deps.Template.templateFeign) { isChanging = true }
    api(Deps.Template.templateMybatisPlus)

}
