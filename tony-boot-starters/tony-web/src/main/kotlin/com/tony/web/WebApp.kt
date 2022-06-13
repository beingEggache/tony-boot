@file:Suppress("unused")

package com.tony.web

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.Beans.getBeanByLazy
import com.tony.Env
import com.tony.Env.getPropertyByLazy
import com.tony.utils.sanitizedPath
import com.tony.web.config.WebProperties
import org.springframework.boot.web.servlet.error.ErrorAttributes

object WebApp : HaveWhiteListUrlPattern {

    internal val errorAttributes: ErrorAttributes by getBeanByLazy()

    private val webProperties: WebProperties by getBeanByLazy()

    @JvmStatic
    val appId: String by getPropertyByLazy("spring.application.name", "")

    @JvmStatic
    val port: String by getPropertyByLazy("server.port", "8080")

    @JvmStatic
    val contextPath: String by getPropertyByLazy("server.servlet.context-path", "")

    @JvmStatic
    val actuatorBasePath: String by getPropertyByLazy("management.endpoints.web.base-path", "/actuator")

    internal val responseWrapExcludePatterns by lazy {
        setOf(
            *(whiteUrlPatterns(prefix = contextPath)).toTypedArray(),
            *webProperties.responseWrapExcludePatterns.map { sanitizedPath("$contextPath/$it") }.toTypedArray()
        )
    }

    private val errorPath by getPropertyByLazy("server.error.path", Env.getProperty("error.path", "/error"))

    @JvmOverloads
    @JvmStatic
    fun errorResponse(msg: String = "", code: Int = ApiProperty.errorCode) =
        ApiResult(EMPTY_RESULT, code, msg)

    @JvmOverloads
    @JvmStatic
    fun badRequest(msg: String = "", code: Int = ApiProperty.validationErrorCode) =
        ApiResult(EMPTY_RESULT, code, msg)
}
