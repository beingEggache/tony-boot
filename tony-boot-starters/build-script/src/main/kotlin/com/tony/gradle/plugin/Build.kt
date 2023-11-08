package com.tony.gradle.plugin

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Build is
 * @author tangli
 * @date 2023/11/08 14:00
 * @since 1.0.0
 */
class Build : Plugin<Project> {
    override fun apply(target: Project) = Unit

    companion object {
        const val GROUP = "com.tony"
        const val PREFIX = "tony"
        const val VERSION = "0.1-SNAPSHOT"

        @JvmStatic
        fun templateProject(name: String): String =
            "$GROUP:$PREFIX-$name:$VERSION"

        @JvmStatic
        fun getProfile(): String =
            System.getProperty("profile", "dev")

        fun Project.getImageNameFromProperty(): String =
            System.getProperty("project_name", name)


        fun Project.copyProjectHookToGitHook(projectRootDir: File, vararg hookNames: String) {

            val gitDir = File(projectRootDir, "/.git/")
            if (!gitDir.exists()) {
                logger.warn("Your project does not has a git directory.")
                return
            }
            val gitHookDir = File(gitDir, "hooks")
            if (!gitHookDir.exists()) {
                logger.warn("Your project does not has a git hook directory.")
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
    }
}
