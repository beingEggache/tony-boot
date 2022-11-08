import com.tony.buildscript.Deps
import com.tony.buildscript.addTestDependencies
dependencies {
    api(projects.tonyCore)

    api(Deps.Aliyun.aliyunJavaSdkDysmsapi)

    implementation(Deps.SpringBoot.springBoot)

    implementation(Deps.Other.annotationApi)
    implementation(Deps.Aliyun.aliyunJavaSdkCore)

    addTestDependencies()
}
