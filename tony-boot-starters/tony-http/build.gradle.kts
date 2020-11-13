import com.tony.build.Deps
import com.tony.build.apiOf

apply(plugin = "maven.publish")

dependencies {

    apiOf(
        project(":tony-core"),
        Deps.Other.httpclient
    )

}
