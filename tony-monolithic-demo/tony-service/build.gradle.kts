import com.tony.gradle.plugin.Build

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
dependencies {
    api(Build.templateProject("core")) { isChanging = true }
    api(Build.templateProject("mybatis-plus")) { isChanging = true }
    api(Build.templateProject("redis")) { isChanging = true }
    api(Build.templateProject("feign")) { isChanging = true }
    implementation(Build.templateProject("snowflake-id")) { isChanging = true }
    implementation(tonyLibs.mysql)
    api(project(":tony-dto"))
    api(tonyLibs.springWeb)
    implementation("com.github.houbb:pinyin:0.4.0")
}
