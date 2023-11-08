import com.tony.gradle.plugin.Build

dependencies {
    api(Build.templateProject("core")) { isChanging = true }
    api(Build.templateProject("mybatis-plus")) { isChanging = true }

    implementation(tonyLibs.mysql)
    implementation(Build.templateProject("snowflake-id")) { isChanging = true }
    implementation(Build.templateProject("redis")) { isChanging = true }
    implementation(Build.templateProject("feign")) { isChanging = true }
    api(project(":tony-dto"))
    api(tonyLibs.mybatisPlusBootStarter)
    api(tonyLibs.springWeb)
}
