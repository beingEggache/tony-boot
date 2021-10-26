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

configure<PublishingExtension> {
    repositories {

        maven {
            name = "private"
            val releasesRepoUrl: String by project
            val snapshotsRepoUrl: String by project
            val nexusUsername: String by project
            val nexusPassword: String by project

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
            register("binary", MavenPublication::class) {
                from(components["java"])
            }

            register("binaryAndSources", MavenPublication::class) {
                from(components["java"])
                artifact(tasks["sourcesJar"])
            }

        } else {
            register("pom", MavenPublication::class) {
                from(components["javaPlatform"])
            }
        }
    }
}
