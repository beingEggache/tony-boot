/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.gradle.plugin

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Build is
 * @author tangli
 * @date 2023/11/08 19:00
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
