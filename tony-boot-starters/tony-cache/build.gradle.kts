import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    api(projects.tonyCore)

    implementation(Deps.SpringBoot.starterDataRedis)
    implementation(Deps.SpringBoot.starterAop)

    addTestDependencies()
    testApi(Deps.SpringBoot.starterDataRedis)
    testApi(Deps.SpringBoot.starterAop)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
