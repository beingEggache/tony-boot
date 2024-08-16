import com.tony.gradle.plugin.Build.Companion.templateProject

apply(plugin = rootProject.tonyLibs.plugins.kotlinSpring.get().pluginId)
dependencies {
    api(templateProject("core")) { isChanging = true }
    api(templateProject("mybatis-plus")) { isChanging = true }
    api(templateProject("redis")) { isChanging = true }
    api(templateProject("feign")) { isChanging = true }
    implementation(templateProject("snowflake-id")) { isChanging = true }
    implementation(tonyLibs.mysql)
    api(project(":tony-dto"))
    api(tonyLibs.springWeb)
    implementation("com.github.houbb:pinyin:0.4.0")
}
