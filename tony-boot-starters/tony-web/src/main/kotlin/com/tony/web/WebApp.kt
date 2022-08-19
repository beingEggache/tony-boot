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
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorAttributes

object WebApp : HaveWhiteListUrlPattern {

    internal val errorAttributes: ErrorAttributes by getBeanByLazy()

    private val webProperties: WebProperties by getBeanByLazy()

    private val logger = LoggerFactory.getLogger(WebApp::class.java)

    @JvmStatic
    val appId: String by getPropertyByLazy("spring.application.name", "")

    @JvmStatic
    val port: String by getPropertyByLazy("server.port", "8080")

    @JvmStatic
    val contextPath: String by getPropertyByLazy("server.servlet.context-path", "")

    internal val responseWrapExcludePatterns by lazy {
        val contextPath = Env.getProperty("server.servlet.context-path", "")
        val set = setOf(
            *whiteUrlPatterns(prefix = contextPath).toTypedArray(),
            *webProperties.responseWrapExcludePatterns.map { sanitizedPath("$contextPath/$it") }.toTypedArray()
        )
        logger.info("Response Wrap Exclude Pattern are: $set")
        set
    }

    @JvmOverloads
    @JvmStatic
    fun errorResponse(msg: String = "", code: Int = ApiProperty.errorCode) =
        ApiResult(EMPTY_RESULT, code, msg)

    @JvmOverloads
    @JvmStatic
    fun badRequest(msg: String = "", code: Int = ApiProperty.validationErrorCode) =
        ApiResult(EMPTY_RESULT, code, msg)
}
