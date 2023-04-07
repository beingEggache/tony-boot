import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    api(projects.tonyCore)
    api(Deps.Other.xxlJob)
    implementation(Deps.SpringBoot.autoconfigure)
    addTestDependencies()
}
