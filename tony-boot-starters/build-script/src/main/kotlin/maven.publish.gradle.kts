plugins {
    `maven-publish`
}

val isPom = project.ext.has("pom")

if (!isPom) {
    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
    }
}

val releasesRepoUrl: String by project
val snapshotsRepoUrl: String by project
val nexusUsername: String by project
val nexusPassword: String by project
configure<PublishingExtension> {
    repositories {
        maven {
            name = "private"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
    publications {
        if (!isPom) {
            register("jar", MavenPublication::class) {
                from(components["kotlin"])
            }

            register("jarAndSrc", MavenPublication::class) {
                from(components["kotlin"])
                artifact(tasks["sourcesJar"])
            }

        } else {
            register("pom", MavenPublication::class) {
                from(components["javaPlatform"])
            }
        }
    }
}
