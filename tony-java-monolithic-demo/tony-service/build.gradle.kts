import com.tony.gradle.plugin.Build.Companion.templateProject

dependencies {
    api(templateProject("core")) { isChanging = true }
    api(templateProject("mybatis-plus")) { isChanging = true }

    implementation(tonyLibs.mysql)
    implementation(templateProject("snowflake-id")) { isChanging = true }
    implementation(templateProject("redis")) { isChanging = true }
    implementation(templateProject("feign")) { isChanging = true }
    api(project(":tony-dto"))
    api(tonyLibs.mybatisPlusBootStarter)
    api(tonyLibs.springWeb)
}
