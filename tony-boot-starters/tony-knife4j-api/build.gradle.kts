import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyCore)
    api(Deps.Knife4j.core)
    api(Deps.Other.swaggerV3Annotaion)
    api(Deps.Other.springdocStarterCommon)

    implementation(Deps.Knife4j.openapi3JakartaSpringBootStarter) {
        exclude("org.springdoc","springdoc-openapi-starter-webflux-ui")
        exclude("org.webjars","swagger-ui")
        exclude("org.webjars","webjars-locator-core")

    }
    implementation(Deps.SpringBoot.autoconfigure)
    addTestDependencies()
    testImplementation(projects.tonyWeb)
    testImplementation(Deps.Knife4j.openapi3Ui)
    testImplementation("com.alibaba:easyexcel:3.3.2")
}
