import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    implementation(projects.tonyCore)
    implementation(Deps.SpringData.starterRedis)
    implementation(Deps.SpringBoot.starterAop)
    compileOnly("io.protostuff:protostuff-core:1.8.0")
    compileOnly("io.protostuff:protostuff-runtime:1.8.0")

    addTestDependencies()
    testApi(Deps.SpringData.starterRedis)
    testApi(Deps.SpringBoot.starterAop)
    testImplementation("io.protostuff:protostuff-core:1.8.0")
    testImplementation("io.protostuff:protostuff-runtime:1.8.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
