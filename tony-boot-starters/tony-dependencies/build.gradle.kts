val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
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
    add("api", platform(libs.springCloudDependencies))
    add("api", platform(libs.springCloudAlibabaDenpendencies))
}
