@file:Suppress("unused")

package com.tony.webcore

import com.tony.core.OK
import com.tony.core.UNAUTHORIZED
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.core.env.Environment
import org.springframework.core.env.getProperty
import org.springframework.stereotype.Component
import java.util.regex.Pattern
import javax.annotation.Resource

@Component
@ConditionalOnWebApplication
object WebApp {

    private lateinit var environment: Environment

    internal lateinit var errorAttributes: ErrorAttributes

    val appId: String by lazy {
        environment.getProperty("web.app-id", "")
    }

    internal val contextPath: String by lazy {
        environment.getProperty("server.servlet.context-path", "")
    }

    internal val excludeJsonResultUrlPatterns by lazy {
        val actuatorBasePath =
            environment.getProperty("management.endpoints.web.base-path", "/actuator")
        val actuatorPrefix = removeDuplicateSlash("$contextPath/$actuatorBasePath")
        listOf(
            removeDuplicateSlash("$contextPath/swagger-resources"),
            removeDuplicateSlash("$contextPath/swagger-resources/configuration/*"),
            removeDuplicateSlash("$contextPath/v2/api-docs*"),
            removeDuplicateSlash("$contextPath/**/*.js"),
            removeDuplicateSlash("$contextPath/**/*.css"),
            removeDuplicateSlash("$actuatorPrefix/beans"),
            removeDuplicateSlash("$actuatorPrefix/caches"),
            removeDuplicateSlash("$actuatorPrefix/caches/*"),
            removeDuplicateSlash("$actuatorPrefix/health"),
            removeDuplicateSlash("$actuatorPrefix/health/*"),
            removeDuplicateSlash("$actuatorPrefix/info"),
            removeDuplicateSlash("$actuatorPrefix/conditions"),
            removeDuplicateSlash("$actuatorPrefix/shutdown"),
            removeDuplicateSlash("$actuatorPrefix/configprops"),
            removeDuplicateSlash("$actuatorPrefix/env"),
            removeDuplicateSlash("$actuatorPrefix/env/*"),
            removeDuplicateSlash("$actuatorPrefix/loggers"),
            removeDuplicateSlash("$actuatorPrefix/loggers/*"),
            removeDuplicateSlash("$actuatorPrefix/heapdump"),
            removeDuplicateSlash("$actuatorPrefix/threaddump"),
            removeDuplicateSlash("$actuatorPrefix/metrics"),
            removeDuplicateSlash("$actuatorPrefix/metrics/*"),
            removeDuplicateSlash("$actuatorPrefix/scheduledtasks"),
            removeDuplicateSlash("$actuatorPrefix/mappings"),
            removeDuplicateSlash("$contextPath/$errorPath")
        )
    }

    private val errorPath by lazy {
        environment.getProperty(
            "server.error.path",
            environment.getProperty("error.path", "/error")
        )
    }

    fun ignoreUrlPatterns(ignoreContextPath: Boolean = false): List<String> {
        val prefix = if (ignoreContextPath)"" else contextPath
        return listOf(
            removeDuplicateSlash("$prefix/actuator/**"),
            removeDuplicateSlash("$prefix/$errorPath"),
            removeDuplicateSlash("$prefix/swagger-resources**"),
            removeDuplicateSlash("$prefix/swagger-resources/**"),
            removeDuplicateSlash("$prefix/webjars/**"),
            removeDuplicateSlash("$prefix/v2/api-docs**"),
            removeDuplicateSlash("$prefix/error"),
            removeDuplicateSlash("$prefix/doc.html"),
            removeDuplicateSlash("$prefix/favicon.ico")
        )
    }

    private val duplicateSlash = Pattern.compile("/{2,}")

    val successCode by lazy {
        environment.getProperty<Int>("web.success-code") ?: OK
    }

    val unauthorizedCode by lazy {
        environment.getProperty<Int>("web.unauthorized-code") ?: UNAUTHORIZED
    }

    @Resource
    private fun environment(environment: Environment) {
        WebApp.environment = environment
    }

    @Resource
    private fun errorAttributes(errorAttributes: ErrorAttributes) {
        WebApp.errorAttributes = errorAttributes
    }

    private fun removeDuplicateSlash(input: String): String =
        duplicateSlash.matcher(input).replaceAll("/")
}
