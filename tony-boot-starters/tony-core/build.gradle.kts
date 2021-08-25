apply {
    plugin("maven.publish")
    plugin("ktlint")
}

dependencies {
    api(Deps.Kotlin.stdlib)
    api(Deps.Other.commonsCodec)
    api(Deps.Other.validationApi)

    api(Deps.Spring.context)
    api(Deps.SpringBoot.starterLogging)

    api(Deps.Jackson.annotations)
    api(Deps.Jackson.core)
    api(Deps.Jackson.databind)
    api(Deps.Jackson.datatypeJdk8)
    api(Deps.Jackson.datatypeJsr310)
    api(Deps.Jackson.moduleKotlin)
    api(Deps.Jackson.moduleParameterNames)

    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}


