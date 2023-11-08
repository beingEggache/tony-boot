import com.tony.gradle.Deps

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.xxlJob)

    implementation(Deps.SpringBoot.autoconfigure)
}
