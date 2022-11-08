import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    api(Deps.Kotlin.stdlib)


    api(Deps.Spring.context)
    api(Deps.SpringBoot.starterLogging) {
        exclude("org.apache.logging.log4j","log4j-to-slf4j")
    }

    api(Deps.Other.validationApi)

    api(Deps.Jackson.datatypeJdk8)
    api(Deps.Jackson.datatypeJsr310)
    api(Deps.Jackson.moduleKotlin)
    api(Deps.Jackson.moduleParameterNames)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}


