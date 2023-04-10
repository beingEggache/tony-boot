import com.tony.buildscript.Deps
dependencies {

    api(Deps.Template.templateCore) { isChanging = true }
    api(Deps.Template.templateMybatisPlus) { isChanging = true }
    implementation(Deps.Other.mysql)
    implementation(Deps.Template.templateCache) { isChanging = true }
    implementation(Deps.Template.templateFeign) { isChanging = true }
    implementation(Deps.Template.templateWechat) { isChanging = true }
//    implementation(Deps.Template.templateAlipay) { isChanging = true }
//    implementation(Deps.Template.templateAliyunOss) { isChanging = true }
//    implementation(Deps.Template.templateAliyunSms) { isChanging = true }
//    implementation(Deps.Template.templateId) { isChanging = true }
//    implementation(Deps.Template.templateXxlJob) { isChanging = true }
    api(project(":tony-dto"))
    api(Deps.Other.mybatisPlusBootStarter)
    api(Deps.Spring.web)

}
