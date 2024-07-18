import com.tony.gradle.plugin.Build

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
dependencies {
    api(Build.templateProject("core")) { isChanging = true }
    api(Build.templateProject("mybatis-plus")) { isChanging = true }
    implementation(Build.templateProject("redis")) { isChanging = true }
    implementation(Build.templateProject("feign")) { isChanging = true }
    implementation(tonyLibs.mysql)
    implementation(tonyLibs.hikariCP)
    implementation(tonyLibs.mybatisTypehandlersJsr310)
}
