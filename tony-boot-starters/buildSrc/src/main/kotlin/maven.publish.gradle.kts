plugins {
    `maven-publish`
}

val sourceSets: SourceSetContainer = extensions.getByType(SourceSetContainer::class.java)
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

configure<PublishingExtension> {
    repositories {
        maven {

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
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
