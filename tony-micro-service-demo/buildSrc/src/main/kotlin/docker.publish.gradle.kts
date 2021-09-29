import com.palantir.gradle.docker.DockerExtension
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.palantir.docker")
    id("org.springframework.boot")
}

val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
val dockerRegistry: String by project
val dockerUserName: String by project
val dockerPassword: String by project

tasks.register("dockerLogin", Exec::class) {
    group = "docker"
    executable = "docker"
    args(
        listOf(
            "login",
            "-u",
            dockerUserName,
            "-p",
            dockerPassword,
            dockerRegistry
        )
    )
}

configure<DockerExtension> {
    //final name:my.registry.com/username/my-app:version
    name = "$dockerRegistry/$dockerUserName/${getImageName()}"
    val today = yyyyMMdd.format(Date())
    tag("today", "$name:$today")
    tag("latest", "$name:latest")
    setDockerfile(File("Dockerfile"))
    // implicit task dep
    val outputs = tasks.getByPath("bootJar").outputs
    copySpec
        .from(outputs)
        .into("")
    buildArgs(
        mapOf(
            "JAR_FILE" to outputs.files.singleFile.name
        )
    )
}

tasks.getByName("docker") {
    dependsOn("dockerLogin")
}
