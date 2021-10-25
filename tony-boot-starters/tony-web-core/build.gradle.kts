dependencies {
    api(projects.tonyCore)
    api(Deps.SpringBoot.starterWeb){
        val list = Deps.SpringBoot.starterTomcat.split(":")
        exclude(list[0],list[1])
    }
    api(Deps.SpringBoot.starterUndertow)
    api(Deps.SpringBoot.starterValidation)
}
