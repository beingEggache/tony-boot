apply(plugin = "maven.publish")

dependencies {
    apiOf(
        Deps.Kotlin.stdlib,
        Deps.Other.commonsCodec,
        Deps.SpringBoot.starterLogging,
        Deps.Other.validationApi,
        Deps.Jackson.annotations,
        Deps.Jackson.core,
        Deps.Jackson.databind,
        Deps.Jackson.datatypeJdk8,
        Deps.Jackson.datatypeJsr310,
        Deps.Jackson.moduleKotlin,
        Deps.Jackson.moduleParameterNames
    )
}


