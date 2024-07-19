catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

tasks.register("clean", Delete::class) {
    group = "build"
    delete("build")
}
