import com.tony.build.Deps
import com.tony.build.addTestDependencies
import com.tony.build.apiOf

apply {
    plugin("kotlin-spring")
    plugin("maven.publish")
}

dependencies {

    apiOf(
        project(":tony-core"),
        Deps.Other.aliyunSdkOss
    )
    implementation(Deps.SpringBoot.springBoot)
    addTestDependencies()
}
