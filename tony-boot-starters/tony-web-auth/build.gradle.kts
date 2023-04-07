import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyJwt)
    api(projects.tonyWeb)
    addTestDependencies()

    testImplementation(Deps.Other.swaggerV3Annotaion)
    testImplementation(Deps.Knife4j.core)
    testImplementation(Deps.Template.templateKnife4j)
    testImplementation(Deps.Knife4j.openapi3Ui)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
