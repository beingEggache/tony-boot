import com.tony.gradle.Deps

dependencies {
    api(projects.tonyCore)
    api(projects.tonyInterfaces)
    api(Deps.SpringBoot.starterWeb) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api(Deps.SpringBoot.starterUndertow)
    implementation(Deps.SpringBoot.starterValidation)

    testImplementation(projects.tonyKnife4jApi)
    testImplementation(Deps.Knife4j.openapi3Ui)
}
