import com.tony.buildscript.Deps
dependencies {

    api(Deps.Template.templateCore) { isChanging = true }
    api(Deps.Template.templateMybatisPlus) { isChanging = true }
    implementation(Deps.Other.mysql)
    implementation(Deps.Template.templateCache) { isChanging = true }
    api(project(":tony-dto"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
