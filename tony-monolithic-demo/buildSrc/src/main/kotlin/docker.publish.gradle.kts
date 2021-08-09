import com.palantir.gradle.docker.DockerExtension
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.palantir.docker")
    id("org.springframework.boot")
}

tasks.register("unpack", Copy::class) {
    dependsOn("bootJar")
    group = "docker"
    from(zipTree(tasks.getByPath("bootJar").outputs.files.singleFile))
    into("build/unpack")
    doLast {
        file("build/unpack/BOOT-INF/classes")
            .renameTo(file("build/unpack/app"))
        file("build/unpack/BOOT-INF/lib")
            .renameTo(file("build/unpack/app/libs"))
    }
}

val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
val dockerRegistry: String by project
val dockerUserName: String by project
val dockerPassword: String by project

tasks.register("dockerLogin", Exec::class) {
    group = "docker"
    executable = "docker"
    args(listOf(
        "login",
        "-u",
        dockerUserName,
        "-p",
        dockerPassword,
        dockerRegistry))
}

configure<DockerExtension> {
    //final name:my.registry.com/username/my-app:version
    name = "$dockerRegistry/$dockerUserName/${getImageName()}"
    val today = yyyyMMdd.format(Date())
    tag("today", "$name:$today")
    tag("latest", "$name:latest")
    setDockerfile(File("Dockerfile"))
    copySpec.from(tasks.getByPath("unpack").outputs).into("dependency")
    buildArgs(mapOf(
        "DEPENDENCY" to "dependency"
    ))
}

tasks.getByName("docker") {
    dependsOn("dockerLogin")
}
