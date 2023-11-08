import com.tony.gradle.Deps

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.mybatisPlusBootStarter3)

    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)

    testImplementation(Deps.Other.mysql)
}
