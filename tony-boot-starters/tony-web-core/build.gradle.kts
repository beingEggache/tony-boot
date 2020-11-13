import com.tony.build.Deps
import com.tony.build.apiOf

apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    api(project(":tony-core"))
    apiOf(
        Deps.Other.javaJwt,
        Deps.SpringBoot.starterWeb,
        Deps.SpringBoot.starterValidation
    )
}
