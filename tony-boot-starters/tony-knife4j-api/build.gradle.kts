import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyCore)
    api(Deps.Knife4j.core)
    api(Deps.Other.swaggerV3Annotaion)
    api(Deps.Other.springdocStarterCommon)

    implementation(Deps.Knife4j.openapi3JakartaSpringBootStarter)
    implementation(Deps.SpringBoot.autoconfigure)

    testImplementation(projects.tonyWeb)
    testImplementation(Deps.Knife4j.openapi3Ui)
    testImplementation("com.alibaba:easyexcel:3.3.2")
}
