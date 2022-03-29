dependencies {
    api(Deps.Kotlin.stdlib)
    api(Deps.Other.commonsCodec)
    api(Deps.Other.validationApi)

    api(Deps.Spring.context)
    api(Deps.SpringBoot.starterLogging)
    api(Deps.Other.annotationApi)

    api(Deps.Jackson.annotations)
    api(Deps.Jackson.core)
    api(Deps.Jackson.databind)
    api(Deps.Jackson.datatypeJdk8)
    api(Deps.Jackson.datatypeJsr310)
    api(Deps.Jackson.moduleKotlin)
    api(Deps.Jackson.moduleParameterNames)

    addTestDependencies()
    testApi("com.github.yitter:yitter-idgenerator:1.0.6")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}


