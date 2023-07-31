package com.tony.web

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.SpringContexts.Env
import com.tony.SpringContexts.getBeanByLazy
import com.tony.utils.sanitizedPath
import com.tony.web.config.WebProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorAttributes

/**
 * Web 全局变量
 *
 */
public object WebApp {

    private const val SWAGGER_UI_PATH: String = "springdoc.swagger-ui.path"
    private const val SWAGGER_UI_PATH_VALUE: String = "/swagger-ui.html"

    private const val SWAGGER_UI_CONFIG: String = "springdoc.swagger-ui.config-url"
    private const val SWAGGER_UI_CONFIG_VALUE: String = "/v3/api-docs/swagger-config"

    private const val SWAGGER_UI_OAUTH2_REDIRECT_URL: String = "springdoc.swagger-ui.oauth2-redirect-url"
    private const val SWAGGER_UI_OAUTH2_REDIRECT_URL_VALUE: String = "/swagger-ui/oauth2-redirect.html"

    private const val SPRINGDOC_API_DOCS_PATH: String = "springdoc.api-docs.path"
    private const val SPRINGDOC_API_DOCS_PATH_VALUE: String = "/v3/api-docs"

    internal val errorAttributes: ErrorAttributes by getBeanByLazy()

    private val webProperties: WebProperties by getBeanByLazy()

    private val logger = LoggerFactory.getLogger(WebApp::class.java)

    /**
     * spring.application.name
     */
    @JvmStatic
    public val appId: String by Env.getPropertyByLazy("spring.application.name", "")

    /**
     * server.port
     */
    @JvmStatic
    public val port: String by Env.getPropertyByLazy("server.port", "8080")

    /**
     * server.servlet.context-path
     */
    @JvmStatic
    public val contextPath: String by Env.getPropertyByLazy("server.servlet.context-path", "")

    internal val responseWrapExcludePatterns by lazy {
        val set = setOf(
            *whiteUrlPatternsWithContextPath.toTypedArray(),
            *webProperties.wrapResponseExcludePatterns.map { sanitizedPath("$contextPath/$it") }.toTypedArray()
        )
        logger.info("Response Wrap Exclude Pattern are: $set")
        set
    }

    /**
     * 全局框架处理层面白名单, 比如不经过全局响应结构包装, 不记录请求日志的 url.
     *
     * 一般默认包含 文档地址, 及对应的静态文件地址.
     */
    @JvmStatic
    public val whiteUrlPatterns: Set<String> by lazy {
        whiteUrlPatterns()
    }

    /**
     * 全局框架处理层面白名单, 比如不经过全局响应结构包装, 不记录请求日志的 url.
     *
     * 一般默认包含 文档地址, 及对应的静态文件地址.
     *
     * 将 [ HttpServletRequest.getContextPath] 包含进去.
     */
    @JvmStatic
    public val whiteUrlPatternsWithContextPath: Set<String> by lazy {
        whiteUrlPatterns(contextPath)
    }

    /**
     * 返回错误响应
     *
     * @param msg
     * @param code 默认为 [ApiProperty.errorCode]
     * @return
     */
    @JvmOverloads
    @JvmStatic
    public fun errorResponse(msg: String = "", code: Int = ApiProperty.errorCode): ApiResult<*> =
        ApiResult(EMPTY_RESULT, code, msg)

    /**
     * 返回请求错误响应
     *
     * @param msg
     * @param code 默认为 [ApiProperty.badRequestCode]
     * @return
     */
    @JvmOverloads
    @JvmStatic
    public fun badRequest(msg: String = "", code: Int = ApiProperty.badRequestCode): ApiResult<*> =
        ApiResult(EMPTY_RESULT, code, msg)

    private fun whiteUrlPatterns(prefix: String = ""): Set<String> {
        val actuatorBasePath = Env.getProperty("management.endpoints.web.base-path", "/actuator")
        val actuatorPrefix = sanitizedPath("$prefix/$actuatorBasePath")
        val errorPath = Env.getProperty("server.error.path", Env.getProperty("error.path", "/error"))
        return setOf(
            sanitizedPath("$actuatorPrefix/**"),
            sanitizedPath("$prefix/$errorPath"),
            sanitizedPath("$prefix/swagger-resources/**"),
            sanitizedPath("$prefix/v2/api-docs/**"),
            sanitizedPath("$prefix/v3/api-docs/**"),
            sanitizedPath("$prefix/webjars/**"),
            sanitizedPath("$prefix/**/*.html"),
            sanitizedPath("$prefix/**/*.ico"),
            sanitizedPath("$prefix/**/*.png"),
            sanitizedPath("$prefix/**/*.js"),
            sanitizedPath("$prefix/**/*.js.map"),
            sanitizedPath("$prefix/**/*.css"),
            sanitizedPath("$prefix/**/*.css.map"),
            sanitizedPath(Env.getProperty(SWAGGER_UI_PATH, SWAGGER_UI_PATH_VALUE)),
            sanitizedPath(Env.getProperty(SWAGGER_UI_CONFIG, SWAGGER_UI_CONFIG_VALUE)),
            sanitizedPath(Env.getProperty(SWAGGER_UI_OAUTH2_REDIRECT_URL, SWAGGER_UI_OAUTH2_REDIRECT_URL_VALUE)),
            sanitizedPath(Env.getProperty(SPRINGDOC_API_DOCS_PATH, SPRINGDOC_API_DOCS_PATH_VALUE))
        )
    }
}
