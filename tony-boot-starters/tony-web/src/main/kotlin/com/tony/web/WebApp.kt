package com.tony.web

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.Env
import com.tony.Env.getPropertyByLazy
import com.tony.SpringContexts.getBeanByLazy
import com.tony.utils.sanitizedPath
import com.tony.web.config.WebProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorAttributes

public object WebApp : HaveWhiteListUrlPattern {

    internal val errorAttributes: ErrorAttributes by getBeanByLazy()

    private val webProperties: WebProperties by getBeanByLazy()

    private val logger = LoggerFactory.getLogger(WebApp::class.java)

    @JvmStatic
    public val appId: String by getPropertyByLazy("spring.application.name", "")

    @JvmStatic
    public val port: String by getPropertyByLazy("server.port", "8080")

    @JvmStatic
    public val contextPath: String by getPropertyByLazy("server.servlet.context-path", "")

    internal val responseWrapExcludePatterns by lazy {
        val contextPath = Env.getProperty("server.servlet.context-path", "")
        val set = setOf(
            *whiteUrlPatterns(prefix = contextPath).toTypedArray(),
            *webProperties.responseWrapExcludePatterns.map { sanitizedPath("$contextPath/$it") }.toTypedArray(),
        )
        logger.info("Response Wrap Exclude Pattern are: $set")
        set
    }

    @JvmOverloads
    @JvmStatic
    public fun errorResponse(msg: String = "", code: Int = ApiProperty.errorCode): ApiResult<Map<Any?, Any?>> =
        ApiResult(EMPTY_RESULT, code, msg)

    @JvmOverloads
    @JvmStatic
    public fun badRequest(msg: String = "", code: Int = ApiProperty.validationErrorCode): ApiResult<Map<Any?, Any?>> =
        ApiResult(EMPTY_RESULT, code, msg)
}
