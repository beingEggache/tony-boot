import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyCore)
    api(Deps.Other.javaJwt)

    implementation(Deps.SpringBoot.autoconfigure)
    implementation(Deps.Other.annotationApi)
}
