import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies

dependencies {

    api(projects.tonyCore)
    api(projects.tonyAnnotations)
    api("org.springframework.cloud:spring-cloud-starter-openfeign"){
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
    }
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
    }

    api(Deps.OpenFeign.openFeignOkhttp) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
    }
    api(Deps.OpenFeign.openFeignJackson)

    implementation(Deps.Other.caffeine)

    addTestDependencies()
    testImplementation(projects.tonyWebAuth)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
