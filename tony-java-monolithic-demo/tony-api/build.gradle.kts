import com.tony.gradle.plugin.Build.Companion.profile
import com.tony.gradle.plugin.Build.Companion.templateProject

// https://github.com/palantir/gradle-docker/issues/801
// apply(plugin = rootProject.tonyLibs.plugins.tonyDocker.get().pluginId)
dependencies {
    //while execute gradle task, use -Pprofile=prod
    if (profile() in setOf("qa", "dev")) {
        implementation(templateProject("knife4j-api")) { isChanging = true }
        implementation(tonyLibs.knife4jOpenapi3Ui)
    }
    implementation(templateProject("web")) { isChanging = true }
    implementation(templateProject("web-auth")) { isChanging = true }
    implementation(project(":tony-service"))
}
