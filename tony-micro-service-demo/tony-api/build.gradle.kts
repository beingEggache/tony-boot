apply {
    plugin("kotlin-spring")
}

dependencies {
    implementation(platform("com.alibaba.cloud:spring-cloud-alibaba-dependencies:2021.1"))
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2020.0.3"))
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    api(Deps.Tony.tonyWebcore)
}
