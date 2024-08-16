import com.tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
dependencies {
    api(templateProject("core")) { isChanging = true }
    api(templateProject("mybatis-plus")) { isChanging = true }
    implementation(templateProject("redis")) { isChanging = true }
    implementation(templateProject("feign")) { isChanging = true }
    implementation(tonyLibs.mysql)
    implementation(tonyLibs.hikariCP)
    implementation(tonyLibs.mybatisTypehandlersJsr310)
}
