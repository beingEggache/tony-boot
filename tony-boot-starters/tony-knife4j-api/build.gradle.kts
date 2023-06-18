import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyCore)
    api(Deps.Knife4j.core)
    api(Deps.Other.swaggerV3Annotaion)
    api(Deps.Other.springdocCommon)
    implementation(Deps.Other.springdocKotlin)
    implementation(Deps.Knife4j.openapi3SpringBootStarter) {
        exclude("com.github.xiaoymin", "knife4j-openapi3-ui")
        exclude("org.springdoc","springdoc-openapi-webflux-core")
        exclude("org.webjars","swagger-ui")
    }
    implementation(Deps.SpringBoot.autoconfigure)
    addTestDependencies()
    testImplementation(projects.tonyWeb)
    testImplementation(Deps.Knife4j.openapi3Ui)
    testImplementation("com.alibaba:easyexcel:3.3.2")
}
