import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyWeb)

    addTestDependencies()
    testImplementation(projects.tonyKnife4jApi)
    testImplementation(Deps.Knife4j.openapi3Ui)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
