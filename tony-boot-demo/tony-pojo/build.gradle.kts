dependencies {

    //while execute gradle task, use -Pprofile=prod
    api(Deps.Other.validationApi)
    api(Deps.Other.swaggerAnnotations)
    implementation(Deps.TonyBoot.core) { isChanging = true }

//    implementation(Deps.SpringBoot.starterActuator)
}
