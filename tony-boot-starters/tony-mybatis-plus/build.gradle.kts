import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {

    api(projects.tonyCore)

    api(Deps.Other.mybatisPlusBootStarter)

    implementation(Deps.Other.HikariCP)
    implementation(Deps.Other.mybatisTypehandlersJsr310)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
