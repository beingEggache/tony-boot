import com.github.godfather1103.gradle.entity.AuthConfig
import com.github.godfather1103.gradle.entity.Resource
import com.tony.buildscript.getImageNameFromProperty
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("io.github.godfather1103.docker-plugin")
    id("org.springframework.boot")
}

val yyyyMMdd = SimpleDateFormat("yyyyMMdd")
val dockerRegistry: String by project
val dockerUserName: String by project
val dockerPassword: String by project
val imageNameFromProperty = getImageNameFromProperty()

docker {
    dockerBuildDependsOn.add("bootJar")
    val outputs = tasks.getByPath("bootJar").outputs
    dockerBuildArgs.put("JAR_FILE", outputs.files.singleFile.name)
    dockerDirectory.value(project.projectDir.absolutePath)
    auth.value(AuthConfig(dockerUserName, dockerPassword))
    imageName.value("$dockerRegistry/$dockerUserName/${imageNameFromProperty}")

    val today = yyyyMMdd.format(Date())
    dockerImageTags.add("latest")
    dockerImageTags.add(today)
    pushImageTag.value(System.getProperty("push_tag", "false").isNotBlank())
    pushImage.value(System.getProperty("push", "false").isNotBlank())
    val resource = Resource()
    resource.directory = projectDir.absolutePath + "/build/libs"
    resource.addIncludes(outputs.files.singleFile.name)
    resources.add(resource)
}
