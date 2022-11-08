import com.tony.buildscript.Deps
dependencies {
    api(projects.tonyCore)

    implementation(projects.tonyCache)
    implementation(Deps.SpringBoot.springBootStarter)

    implementation(Deps.Other.yitterIdgenerator)
}
