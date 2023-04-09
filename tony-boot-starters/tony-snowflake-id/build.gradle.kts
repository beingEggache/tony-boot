import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(Deps.Kotlin.stdlib)
    implementation(Deps.SpringBoot.springBootStarter)
    implementation(Deps.Other.yitterIdgenerator)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
