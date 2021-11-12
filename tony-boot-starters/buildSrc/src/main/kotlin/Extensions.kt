import org.gradle.api.Project
import java.io.File

/**
 *
 * @author tangli
 * @since 2021-01-28 9:46
 */
fun Project.copyProjectHookToGitHook(vararg hookNames: String) {

    val gitDir = File(rootDir, "/.git/")
    if (!gitDir.exists()) {
        logger.warn("Your project does not has a git directory.")
        return
    }
    val gitHookDir = File(gitDir, "hooks")
    if (!gitHookDir.exists()) {
        logger.warn("Your project does not has a git directory.")
        return
    }

    hookNames.forEach { hookName ->
        val hookFile = File(getProjectGitHook(hookName))
        if (!hookFile.exists()) {
            logger.warn("Your project src does not exist githook:$hookName.")
            return@forEach
        }

        val gitHookFile = File(gitHookDir, hookName)
        if (gitHookFile.exists()) {
            logger.warn("Your project has already exists githook:$hookName.")
            return@forEach
        }

        hookFile.copyTo(gitHookFile)
        logger.info("$hookName has already copy to ${gitHookDir.absolutePath}")
    }
}

private fun Project.getProjectGitHooksPath(): String {
    val projectGitHooks = File(rootDir, "/githooks/")
    if (!projectGitHooks.exists()) {
        logger.warn("RootProject does not exists githooks directory.")
        return ""
    }
    return projectGitHooks.absolutePath
}

private fun Project.getProjectGitHook(hookName: String): String {
    val hook = File(getProjectGitHooksPath(), hookName)
    if (!hook.exists()) {
        logger.warn("RootProject does not exists githook:$hookName.")
        return ""
    }
    return hook.absolutePath
}

fun getProfile(): String = System.getProperty("profile", "dev")

fun Project.getImageName(): String = System.getProperty("project_name", rootProject.name)
