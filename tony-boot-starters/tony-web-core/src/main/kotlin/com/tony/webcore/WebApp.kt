@file:Suppress("unused")

package com.tony.webcore

import com.tony.core.ApiProperty
import com.tony.core.ApiResult
import com.tony.core.ApiResult.Companion.EMPTY_RESULT
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.regex.Pattern
import javax.annotation.Resource

@ConditionalOnWebApplication
@Component
object WebApp {

    private lateinit var environment: Environment

    @JvmSynthetic
    internal lateinit var errorAttributes: ErrorAttributes

    @JvmStatic
    val appId: String by lazy {
        environment.getProperty("spring.application.name", "")
    }

    internal val contextPath: String by lazy {
        environment.getProperty("server.servlet.context-path", "")
    }

    internal val excludeJsonResultUrlPatterns by lazy {
        val actuatorBasePath =
            environment.getProperty("management.endpoints.web.base-path", "/actuator")
        val actuatorPrefix = removeDuplicateSlash("$contextPath/$actuatorBasePath")
        listOf(
            removeDuplicateSlash("$contextPath/swagger-resources/**"),
            removeDuplicateSlash("$contextPath/v2/api-docs/**"),
            removeDuplicateSlash("$contextPath/**/*.js"),
            removeDuplicateSlash("$contextPath/**/*.css"),
            removeDuplicateSlash("$actuatorPrefix/**"),
            removeDuplicateSlash("$contextPath/$errorPath")
        )
    }

    private val errorPath by lazy {
        environment.getProperty(
            "server.error.path",
            environment.getProperty("error.path", "/error")
        )
    }

    @JvmStatic
    fun ignoreUrlPatterns(ignoreContextPath: Boolean = false): List<String> {
        val prefix = if (ignoreContextPath)"" else contextPath
        val actuatorBasePath =
            environment.getProperty("management.endpoints.web.base-path", "/actuator")
        val actuatorPrefix = removeDuplicateSlash("$prefix/$actuatorBasePath")
        return listOf(
            removeDuplicateSlash("$actuatorPrefix/**"),
            removeDuplicateSlash("$prefix/$errorPath"),
            removeDuplicateSlash("$prefix/swagger-resources/**"),
            removeDuplicateSlash("$prefix/webjars/**"),
            removeDuplicateSlash("$prefix/v2/api-docs/**"),
            removeDuplicateSlash("$prefix/error"),
            removeDuplicateSlash("$prefix/doc.html"),
            removeDuplicateSlash("$prefix/favicon.ico")
        )
    }

    @JvmOverloads
    @JvmStatic
    fun errorResponse(msg: String = "", code: Int = ApiProperty.errorCode) =
        ApiResult(EMPTY_RESULT, code, msg)

    @JvmOverloads
    @JvmStatic
    fun badRequest(msg: String = "", code: Int = ApiProperty.validationErrorCode) =
        ApiResult(EMPTY_RESULT, code, msg)

    @JvmSynthetic
    @Resource
    internal fun environment(environment: Environment) {
        WebApp.environment = environment
    }

    @JvmSynthetic
    @Resource
    internal fun errorAttributes(errorAttributes: ErrorAttributes) {
        WebApp.errorAttributes = errorAttributes
    }

    private val duplicateSlash = Pattern.compile("/{2,}")

    private fun removeDuplicateSlash(input: String): String =
        duplicateSlash.matcher(input).replaceAll("/")
}
