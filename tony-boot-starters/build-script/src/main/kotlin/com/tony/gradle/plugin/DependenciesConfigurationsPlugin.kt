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

import java.util.concurrent.TimeUnit
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType

class DependenciesConfigurationsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configurations.all {

            exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
            exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
            exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
            exclude(group = "tech.powerjob", module = "powerjob-remote-impl-akka")

            exclude(group = "org.springdoc", module = "springdoc-openapi-starter-webflux-ui")
            exclude(group = "org.webjars", module = "swagger-ui")
            exclude(group = "org.webjars", module = "webjars-locator-core")

            exclude(group = "com.google.errorprone", module = "error_prone_annotations")
            exclude(group = "com.google.guava", module = "listenablefuture")
            exclude(group = "com.google.j2objc", module = "j2objc-annotations")
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

        val kotlinVersion = versionCatalog.findVersion("kotlin").get()
        val bouncycastleVersion = versionCatalog.findVersion("bouncycastle").get()
        val annotationApiVersion = versionCatalog.findVersion("annotationApi").get()
        val activationApiVersion = versionCatalog.findVersion("activationApi").get()
        val elApiVersion = versionCatalog.findVersion("elApi").get()
        val websocketApiVersion = versionCatalog.findVersion("websocketApi").get()
        val validationApiVersion = versionCatalog.findVersion("validationApi").get()
        val bindApiVersion = versionCatalog.findVersion("bindApi").get()
        val springVersion = versionCatalog.findVersion("spring").get()
        return mapOf(
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8" to "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7" to "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",

            "bouncycastle:bcprov-jdk14" to "org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion",
            "bouncycastle:bcmail-jdk14" to "org.bouncycastle:bcmail-jdk18on:$bouncycastleVersion",
            "bouncycastle:bctsp-jdk14" to "org.bouncycastle:bctsp-jdk15on:1.46",

            "org.bouncycastle:bcprov-jdk14" to "org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion",
            "org.bouncycastle:bcmail-jdk14" to "org.bouncycastle:bcmail-jdk18on:$bouncycastleVersion",
            "org.bouncycastle:bctsp-jdk14" to "org.bouncycastle:bctsp-jdk15on:1.46",

            "org.bouncycastle:bcprov-jdk15on" to "org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion",
            "org.bouncycastle:bcmail-jdk15on" to "org.bouncycastle:bcmail-jdk18on:$bouncycastleVersion",
            "org.bouncycastle:bcpkix-jdk15on" to "org.bouncycastle:bcpkix-jdk18on:$bouncycastleVersion",

            "org.apache.tomcat:tomcat-annotations-api" to "jakarta.annotation:jakarta.annotation-api:$annotationApiVersion",
            "javax.annotation:javax.annotation-api" to "jakarta.annotation:jakarta.annotation-api:$annotationApiVersion",
            "org.jboss.spec.javax.annotation:jboss-annotations-api_1.3_spec" to "jakarta.annotation:jakarta.annotation-api:$annotationApiVersion",
            "javax.activation:javax.activation-api" to "jakarta.activation:jakarta.activation-api:$activationApiVersion",

            "javax.el:el-api" to "jakarta.el:jakarta.el-api:$elApiVersion",
            "org.glassfish:jakarta.el" to "jakarta.el:jakarta.el-api:$elApiVersion",
            "org.glassfish.web:el-impl" to "org.apache.tomcat.embed:tomcat-embed-el:10.1.25",

            "org.jboss.spec.javax.websocket:jboss-websocket-api_1.1_spec" to "jakarta.websocket:jakarta.websocket-api:$websocketApiVersion",
            "javax.validation:validation-api" to "jakarta.validation:jakarta.validation-api:$validationApiVersion",
            "javax.xml.bind:jaxb-api" to "jakarta.xml.bind:jakarta.xml.bind-api:$bindApiVersion",
            "commons-logging:commons-logging" to "org.springframework:spring-jcl:$springVersion"
        )
    }
}
