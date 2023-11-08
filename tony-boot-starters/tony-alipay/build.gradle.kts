import com.tony.gradle.Deps

dependencies {
    api(projects.tonyCore)
    implementation(Deps.SpringBoot.springBoot)
    implementation(Deps.Aliyun.alipaySdkJava)
}
