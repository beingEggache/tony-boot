import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyWeb)

    testImplementation(projects.tonyKnife4jApi)
    testImplementation(Deps.Knife4j.openapi3Ui)
}
