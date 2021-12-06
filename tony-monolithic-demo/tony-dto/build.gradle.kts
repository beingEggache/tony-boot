dependencies {

    //while execute gradle task, use -Pprofile=prod
    api(Deps.Other.validationApi)
    api(Deps.Other.swaggerV3Annotaion)
    implementation(Deps.Tony.tonyCore) { isChanging = true }
    implementation(Deps.Spring.web)
    implementation(Deps.Spring.context)

//    implementation(Deps.SpringBoot.starterActuator)
}
