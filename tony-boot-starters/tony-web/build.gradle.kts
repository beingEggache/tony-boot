import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyCore)
    api(Deps.SpringBoot.starterWeb)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
