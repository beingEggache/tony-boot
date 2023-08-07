import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.mybatisPlusBootStarter)

    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)

    testImplementation(Deps.Other.mysql)
}
