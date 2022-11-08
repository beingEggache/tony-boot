import com.tony.buildscript.Deps
apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.Template.templateCore) { isChanging = true }
    api(Deps.Template.templateMybatisPlus) { isChanging = true }
    implementation(Deps.Other.mysql)
    implementation(Deps.Template.templateCache) { isChanging = true }
    api(project(":tony-dto"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
