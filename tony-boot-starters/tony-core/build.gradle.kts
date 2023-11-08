import com.tony.gradle.Deps
import com.tony.gradle.addTestDependencies

dependencies {
    api(Deps.Kotlin.stdlib)

    api(Deps.Spring.context)
    api(Deps.SpringBoot.starterLogging)
    api(Deps.Other.validationApi)
    api(Deps.Jackson.datatypeJdk8)
    api(Deps.Jackson.datatypeJsr310)
    api(Deps.Jackson.moduleKotlin)
    api(Deps.Jackson.moduleParameterNames)

    implementation(Deps.Other.bcprovJdk18On)
    compileOnly(Deps.Other.findbugsJsr305)
}
