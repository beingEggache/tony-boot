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

import java.util.concurrent.TimeUnit
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType

class DependenciesConfigurationsPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        target.configurations.all {

            exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
            exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
            exclude(group = "tech.powerjob", module = "powerjob-remote-impl-akka")

            exclude(group = "org.springdoc", module = "springdoc-openapi-starter-webflux-ui")
            exclude(group = "org.webjars", module = "swagger-ui")
            exclude(group = "org.webjars", module = "webjars-locator-core")

            exclude(group = "com.google.errorprone", module = "error_prone_annotations")
            exclude(group = "com.google.guava", module = "listenablefuture")
            exclude(group = "com.google.j2objc", module = "j2objc-annotations")
            exclude(group = "com.google.code.findbugs", module = "jsr305")
            exclude(group = "com.google.code.findbugs", module = "annotations")
            exclude(group = "com.vaadin.external.google", module = "android-json")
            exclude(group = "org.checkerframework", module = "checker-qual")

            resolutionStrategy {
                //disable cache
                cacheChangingModulesFor(0, TimeUnit.NANOSECONDS)
                dependencySubstitution {
                    canReplacedDependencies(target).forEach { (sourceDependency, targetDependency) ->
                        substitute(module(sourceDependency)).using(module(targetDependency))
                    }
                }
            }
        }
    }

    private fun VersionCatalog.libToString(name: String): String = findLibrary(name).get().get().toString()

    private fun canReplacedDependencies(target: Project): Map<String, String> {
        val versionCatalog =
            try {
                target
                    .rootProject
                    .extensions
                    .getByType<VersionCatalogsExtension>()
                    .named("tonyLibs")
            } catch (e: InvalidUserDataException) {
                target.logger.warn(e.message)
                return mapOf()
            }

        val kotlinStdlib = versionCatalog.libToString("kotlinStdlib")
        val springJcl = versionCatalog.libToString("springJcl")
        val tomcatEmbedEl = versionCatalog.libToString("tomcatEmbedEl")

        val bcprovJdk18On = versionCatalog.libToString("bcprovJdk18On")
        val bcmailJdk18On = versionCatalog.libToString("bcmailJdk18On")
        val bcpkixJdk18On = versionCatalog.libToString("bcpkixJdk18On")

        val validationApi = versionCatalog.libToString("validationApi")
        val annotationApi = versionCatalog.libToString("annotationApi")
        val activationApi = versionCatalog.libToString("activationApi")
        val bindApi = versionCatalog.libToString("bindApi")
        val websocketApi = versionCatalog.libToString("websocketApi")
        val elApi = versionCatalog.libToString("elApi")
        val dom4j = versionCatalog.libToString("dom4j")
        return mapOf(
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8" to kotlinStdlib,
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7" to kotlinStdlib,

            "bouncycastle:bcprov-jdk14" to bcprovJdk18On,
            "bouncycastle:bcmail-jdk14" to bcmailJdk18On,

            "org.bouncycastle:bcprov-jdk14" to bcprovJdk18On,
            "org.bouncycastle:bcmail-jdk14" to bcmailJdk18On,

            "org.bouncycastle:bcprov-jdk15on" to bcprovJdk18On,
            "org.bouncycastle:bcmail-jdk15on" to bcmailJdk18On,
            "org.bouncycastle:bcpkix-jdk15on" to bcpkixJdk18On,

            "org.apache.tomcat:tomcat-annotations-api" to annotationApi,
            "javax.annotation:javax.annotation-api" to annotationApi,
            "org.jboss.spec.javax.annotation:jboss-annotations-api_1.3_spec" to annotationApi,
            "javax.activation:javax.activation-api" to activationApi,

            "javax.el:el-api" to elApi,
            "org.glassfish:jakarta.el" to elApi,
            "org.glassfish.web:el-impl" to tomcatEmbedEl,

            "org.jboss.spec.javax.websocket:jboss-websocket-api_1.1_spec" to websocketApi,
            "javax.validation:validation-api" to validationApi,
            "javax.xml.bind:jaxb-api" to bindApi,
            "commons-logging:commons-logging" to springJcl,
            "dom4j:dom4j" to dom4j
        )
    }
}
