import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyCore)
    api(projects.tonyAnnotations)
    api(Deps.SpringBoot.starterWeb)
    implementation(Deps.SpringBoot.starterValidation)

    testImplementation(projects.tonyKnife4jApi)
    testImplementation(Deps.Knife4j.openapi3Ui)
}
