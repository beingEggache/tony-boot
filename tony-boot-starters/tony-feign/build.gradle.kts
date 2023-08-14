import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyCore)
    api(projects.tonyInterfaces)
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    api(Deps.OpenFeign.openFeignOkhttp)
    api(Deps.OpenFeign.openFeignJackson)
    implementation(Deps.Other.caffeine)

    testImplementation(projects.tonyWebAuth)
}
