import com.tony.build.Deps
import com.tony.build.implementationOf

apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    implementationOf(
        Deps.SpringBoot.springBoot,
        Deps.Spring.web,
        Deps.Other.springfoxSwagger2,
        Deps.Other.knife4j
    )
}
