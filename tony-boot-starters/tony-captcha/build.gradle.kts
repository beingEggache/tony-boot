import com.tony.buildscript.Deps
dependencies {
    implementation(projects.tonyCore)
    implementation(projects.tonyCache)
    implementation(Deps.SpringBoot.springBootStarter)
    implementation(Deps.Other.easyCaptcha)
}
