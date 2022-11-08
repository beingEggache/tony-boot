import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    api(projects.tonyFeign)

    api(Deps.Other.xstream)
    implementation(Deps.Other.commonsCodec)

    implementation(Deps.SpringBoot.springBoot)

    addTestDependencies()
    testImplementation(projects.tonyCache)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
