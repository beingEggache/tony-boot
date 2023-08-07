import com.tony.buildscript.Deps

dependencies {
    implementation(projects.tonyCore)
    implementation(projects.tonyRedis)
    implementation(Deps.SpringBoot.springBootStarter)
    implementation(Deps.Other.easyCaptcha)
}
