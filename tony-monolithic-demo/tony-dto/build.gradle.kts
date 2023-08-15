import com.tony.buildscript.Deps

dependencies {

    //while execute gradle task, use -Pprofile=prod
    api(Deps.Other.validationApi)
    api(Deps.Other.swaggerV3Annotaion)
    implementation(Deps.Template.templateCore) { isChanging = true }
    implementation(Deps.Template.templateInterfaces) { isChanging = true }
    implementation(Deps.Spring.web)
    implementation(Deps.Spring.context)
    api("com.alibaba:easyexcel:3.3.2")
//    implementation(Deps.SpringBoot.starterActuator)
}
