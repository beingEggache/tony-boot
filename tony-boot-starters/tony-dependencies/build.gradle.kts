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
    add("api", platform(tonyLibs.springCloudDependencies))
    add("api", platform(tonyLibs.springCloudAlibabaDenpendencies))
}
