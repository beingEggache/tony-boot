import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyCore)
    api(projects.tonyAnnotations)
    api(Deps.SpringBoot.starterWeb)
    implementation(Deps.SpringBoot.starterValidation)

    addTestDependencies()
    testImplementation(projects.tonyKnife4jApi)
    testImplementation(Deps.Knife4j.openapi3Ui)

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
