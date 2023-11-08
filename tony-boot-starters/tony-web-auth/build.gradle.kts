import com.tony.gradle.Deps

dependencies {
    api(projects.tonyJwt)
    api(projects.tonyWeb)

    testImplementation(projects.tonyKnife4jApi)
    testImplementation(Deps.Knife4j.openapi3Ui)
}
