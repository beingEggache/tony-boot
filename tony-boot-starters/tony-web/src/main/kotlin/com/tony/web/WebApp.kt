@file:Suppress("unused")

package com.tony.web

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.Env
import com.tony.web.config.WebProperties
import org.springframework.boot.web.servlet.error.ErrorAttributes
import java.net.InetAddress
import java.util.regex.Pattern

object WebApp {

    internal val errorAttributes: ErrorAttributes by lazy {
        Env.bean()
    }

    private val webProperties: WebProperties by lazy {
        Env.bean()
    }

    @JvmStatic
    val appId: String by lazy {
        Env.prop("spring.application.name", "")
    }

    val ip: String
        @JvmStatic
        get() = InetAddress.getLocalHost().hostAddress

    @JvmStatic
    val port: String by lazy {
        Env.prop("server.port", "8080")
    }

    @JvmStatic
    val contextPath: String by lazy {
        Env.prop("server.servlet.context-path", "")
    }

    @JvmStatic
    val actuatorBasePath: String by lazy {
        Env.prop("management.endpoints.web.base-path", "/actuator")
    }

    val profiles: Array<String> by lazy {
        Env.activeProfiles()
    }

    internal val responseWrapExcludePatterns by lazy {
        setOf(
            *whiteUrlPatterns(prefix = contextPath).toTypedArray(),
            *webProperties.responseWrapExcludePatterns.map { sanitizedPath("$contextPath/$it") }.toTypedArray()
        )
    }

    private val errorPath by lazy {
        Env.prop("server.error.path", Env.prop("error.path", "/error"))
    }

    @JvmStatic
    fun whiteUrlPatterns(prefix: String): Set<String> {
        val actuatorPrefix = sanitizedPath("$prefix/$actuatorBasePath")
        return setOf(
            sanitizedPath("$actuatorPrefix/**"),
            sanitizedPath("$prefix/$errorPath"),
            sanitizedPath("$prefix/swagger-resources/**"),
            sanitizedPath("$prefix/v2/api-docs/**"),
            sanitizedPath("$prefix/webjars/**"),
            sanitizedPath("$prefix/**/*.html"),
            sanitizedPath("$prefix/**/*.ico"),
            sanitizedPath("$prefix/**/*.png"),
            sanitizedPath("$prefix/**/*.js"),
            sanitizedPath("$prefix/**/*.js.map"),
            sanitizedPath("$prefix/**/*.css"),
            sanitizedPath("$prefix/**/*.css.map"),
            *(springdocUrls.map { "$prefix/$it" }.toTypedArray())
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

    private val springdocUrls by lazy {
        arrayOf(
            Env.prop("springdoc.swagger-ui.path", "/swagger-ui.html"),
            Env.prop("springdoc.swagger-ui.config-url", "/v3/api-docs/swagger-config"),
            Env.prop("springdoc.swagger-ui.oauth2-redirect-url", "/swagger-ui/oauth2-redirect.html"),
            Env.prop("springdoc.api-docs.path", "/v3/api-docs")
        )
    }

    private val duplicateSlash = Pattern.compile("/{2,}")

    private fun sanitizedPath(input: String): String =
        duplicateSlash.matcher(input).replaceAll("/")
}
