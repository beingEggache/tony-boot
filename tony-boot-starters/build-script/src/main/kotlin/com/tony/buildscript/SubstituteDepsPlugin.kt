package com.tony.buildscript

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.concurrent.TimeUnit

class SubstituteDepsPlugin : Plugin<Project> {

    private val canReplacedDependencies = mapOf(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8" to "org.jetbrains.kotlin:kotlin-stdlib:${VersionManagement.kotlinVersion}",
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7" to "org.jetbrains.kotlin:kotlin-stdlib:${VersionManagement.kotlinVersion}",

        "bouncycastle:bcprov-jdk14" to "org.bouncycastle:bcprov-jdk18on:${VersionManagement.bouncycastleVersion}",
        "bouncycastle:bcmail-jdk14" to "org.bouncycastle:bcmail-jdk18on:${VersionManagement.bouncycastleVersion}",
        "bouncycastle:bctsp-jdk14" to "org.bouncycastle:bctsp-jdk15on:1.46",

        "org.bouncycastle:bcprov-jdk14" to "org.bouncycastle:bcprov-jdk18on:${VersionManagement.bouncycastleVersion}",
        "org.bouncycastle:bcmail-jdk14" to "org.bouncycastle:bcmail-jdk18on:${VersionManagement.bouncycastleVersion}",
        "org.bouncycastle:bctsp-jdk14" to "org.bouncycastle:bctsp-jdk15on:1.46",

        "org.bouncycastle:bcprov-jdk15on" to "org.bouncycastle:bcprov-jdk18on:${VersionManagement.bouncycastleVersion}",
        "org.bouncycastle:bcmail-jdk15on" to "org.bouncycastle:bcmail-jdk18on:${VersionManagement.bouncycastleVersion}",
        "org.bouncycastle:bcpkix-jdk15on" to "org.bouncycastle:bcpkix-jdk18on:${VersionManagement.bouncycastleVersion}",

        "org.apache.tomcat:tomcat-annotations-api" to "jakarta.annotation:jakarta.annotation-api:1.3.5",
        "javax.annotation:javax.annotation-api" to "jakarta.annotation:jakarta.annotation-api:1.3.5",
        "org.jboss.spec.javax.annotation:jboss-annotations-api_1.3_spec" to "jakarta.annotation:jakarta.annotation-api:1.3.5",
        "javax.activation:javax.activation-api" to "jakarta.activation:jakarta.activation-api:1.2.2",

        "javax.el:el-api" to "jakarta.el:jakarta.el-api:3.0.3",
        "org.glassfish:jakarta.el" to "jakarta.el:jakarta.el-api:3.0.3",
        "org.glassfish.web:el-impl" to "org.apache.tomcat.embed:tomcat-embed-el:9.0.78",

        "org.jboss.spec.javax.websocket:jboss-websocket-api_1.1_spec" to "jakarta.websocket:jakarta.websocket-api:1.1.2",
        "javax.validation:validation-api" to "jakarta.validation:jakarta.validation-api:2.0.2",
        "javax.xml.bind:jaxb-api" to "jakarta.xml.bind:jakarta.xml.bind-api:2.3.3",
        "commons-logging:commons-logging" to "org.springframework:spring-jcl:${VersionManagement.springVersion}"
    )

    override fun apply(target: Project) {
        target.configurations.all {
            resolutionStrategy {
                //disable cache
                cacheChangingModulesFor(0, TimeUnit.NANOSECONDS)
                dependencySubstitution {
                    canReplacedDependencies.forEach { (sourceDependency, targetDependency) ->
                        substitute(module(sourceDependency)).using(module(targetDependency))
                    }
                }
            }
        }
    }
}
