import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyCore)
    api(projects.tonyAnnotations)
    api(Deps.SpringBoot.starterWeb)
    implementation(Deps.SpringBoot.starterValidation)

    addTestDependencies()
    testImplementation(Deps.Template.templateKnife4j)
    testImplementation(Deps.Knife4j.openapi3Ui)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
