ext.set("catalog", true)
apply {
    plugin(rootProject.tonyLibs.plugins.versionCatalog.get().pluginId)
    plugin(rootProject.tonyLibs.plugins.tonyMavenPublish.get().pluginId)
}

extensions.getByType<CatalogPluginExtension>().apply {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

tasks.register("clean", Delete::class) {
    group = "build"
    delete("build")
}
