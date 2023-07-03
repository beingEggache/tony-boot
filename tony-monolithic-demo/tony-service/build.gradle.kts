import com.tony.buildscript.Deps
apply(plugin = "kotlin-spring")
dependencies {

    api(Deps.Template.templateCore) { isChanging = true }
    implementation(Deps.Template.templateRedis) { isChanging = true }

    implementation(Deps.Other.mysql)
    api(Deps.Template.templateMybatisPlus) { isChanging = true }
    implementation(Deps.Template.templateId) { isChanging = true }

    api(project(":tony-dto"))
    api(Deps.Spring.web)

}
