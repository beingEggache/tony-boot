import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    implementation(projects.tonyCore)
    implementation(Deps.SpringData.starterRedis)
    implementation(Deps.SpringBoot.starterAop)

    addTestDependencies()
    testApi(Deps.SpringData.starterRedis)
    testApi(Deps.SpringBoot.starterAop)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
