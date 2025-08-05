import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

ext.set("pom", true)
apply {
    plugin(rootProject.tonyLibs.plugins.javaPlatform.get().pluginId)
    plugin(rootProject.tonyLibs.plugins.tonyMavenPublish.get().pluginId)
    plugin(rootProject.tonyLibs.plugins.gradleVersionsPlugin.get().pluginId)
}

extensions.getByType<JavaPlatformExtension>().apply {
    allowDependencies()
}

val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("tonyLibs")
val libraryDependencies =
    versionCatalog
        .libraryAliases
        .map {
            versionCatalog.findLibrary(it).get()
        }
tasks.withType<DependencyUpdatesTask> {
    revision = "release"
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"
    checkConstraints = true
    checkBuildEnvironmentConstraints = true
    outputFormatter = "plain"
    rejectVersionIf {
        candidate
            .version
            .contains(Regex("alpha|beta|rc|snapshot|milestone|pre|m", RegexOption.IGNORE_CASE))
    }
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
