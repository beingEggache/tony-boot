import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.xxlJob)

    implementation(Deps.SpringBoot.autoconfigure)
}
