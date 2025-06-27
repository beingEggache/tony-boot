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

package tony.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

/**
 * Build is
 * @author tangli
 * @date 2023/11/08 19:00
 */
class Build : Plugin<Project> {
    override fun apply(target: Project) = Unit

    companion object {
        private const val GROUP = "tony"
        private const val PREFIX = "tony"
        private const val VERSION = "0.1-SNAPSHOT"

        @JvmStatic
        fun Project.propFromSysOrProject(propertyName: String, defaultValue: String = ""): String =
            System.getProperty(propertyName) ?: providers.gradleProperty(propertyName).getOrElse(defaultValue)

        @JvmStatic
        fun Settings.propFromSysOrSettings(propertyName: String, defaultValue: String = ""): String =
            System.getProperty(propertyName) ?: providers.gradleProperty(propertyName).getOrElse(defaultValue)

        @JvmStatic
        fun Project.templateGroup(defaultValue: String = GROUP): String =
            propFromSysOrProject("templateGroup", defaultValue)

        @JvmStatic
        fun Project.templatePrefix(defaultValue: String = PREFIX): String =
            propFromSysOrProject("templatePrefix", defaultValue)

        @JvmStatic
        fun Project.templateVersion(defaultValue: String = VERSION): String =
            propFromSysOrProject("templateVersion", defaultValue)

        @JvmStatic
        fun Project.profile(): String =
            propFromSysOrProject("profile", "dev")

        @JvmStatic
        fun Project.templateProject(
            name: String,
            group: String = GROUP,
            prefix: String = PREFIX,
            version: String = VERSION): String =
            "${templateGroup(group)}:${templatePrefix(prefix)}-$name:${templateVersion(version)}"

        @JvmStatic
        fun Settings.templateProject(
            name: String,
            group: String = GROUP,
            prefix: String = PREFIX,
            version: String = VERSION): String =
            "${propFromSysOrSettings("templateGroup", group)}:" +
                "${propFromSysOrSettings("templatePrefix", prefix)}-$name:" +
                propFromSysOrSettings("templateVersion", version)
    }
}
