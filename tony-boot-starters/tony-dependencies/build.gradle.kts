val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("tonyLibs")
val libraryDependencies =
    versionCatalog
        .libraryAliases
        .map {
            versionCatalog.findLibrary(it).get()
        }

dependencies {
    constraints {
        libraryDependencies.forEach {
            add("api", it)
        }
    }
    add("api", platform(tonyLibs.kotlinBom))
    add("api", platform(tonyLibs.springBom))
    add("api", platform(tonyLibs.reactorBom))
    add("api", platform(tonyLibs.nettyBom))
    add("api", platform(tonyLibs.jacksonBom))
    add("api", platform(tonyLibs.grpcBom))
    add("api", platform(tonyLibs.springBootDependencies))
    add("api", platform(tonyLibs.springCloudDependencies))
}
