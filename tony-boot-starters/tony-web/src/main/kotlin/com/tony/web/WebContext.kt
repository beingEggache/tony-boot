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

package com.tony.web

import com.tony.SpringContexts
import com.tony.utils.asTo
import com.tony.utils.asToDefault
import com.tony.utils.asToNotNull
import com.tony.utils.ifNull
import com.tony.utils.sanitizedPath
import com.tony.web.config.WebProperties
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.ServletWebRequest

/**
 * 全局 Web 上下文单例
 *
 * @author tangli
 * @date 2023/05/25 19:58
 */
public data object WebContext {
    /**
     * 获取当前 [ServletRequestAttributes]
     */
    @get:JvmName("current")
    @JvmStatic
    public val current: ServletRequestAttributes
        get() = RequestContextHolder.currentRequestAttributes().asToNotNull()

    /**
     * 当前请求.
     * @see [ServletRequestAttributes.getRequest]
     */
    @get:JvmName("request")
    @JvmStatic
    public val request: HttpServletRequest
        get() = current.request

    /**
     * 当前响应.
     * @see [ServletRequestAttributes.getResponse]
     */
    @get:JvmName("response")
    @JvmStatic
    public val response: HttpServletResponse?
        get() = current.response

    @JvmStatic
    private val errorAttributeOptions =
        ErrorAttributeOptions.of(Include.MESSAGE)

    internal val error: String
        @JvmSynthetic
        get() =
            errorAttributes["error"].asToDefault("")

    internal val errorMessage: String
        @JvmSynthetic
        get() =
            errorAttributes["message"].asToDefault("")

    internal val httpStatus: Int
        @JvmSynthetic
        get() =
            errorAttributes["status"].asToDefault(0)

    @Suppress("MemberVisibilityCanBePrivate")
    internal val errorAttributes
        @JvmSynthetic
        @JvmStatic
        get() =
            current
                .getOrPut("errorAttribute") {
                    errorAttributesBean.getErrorAttributes(ServletWebRequest(request), errorAttributeOptions)
                }

    @get:JvmSynthetic
    internal val errorAttributesBean: ErrorAttributes by SpringContexts.getBeanByLazy()

    /**
     * [ServletRequestAttributes] [SCOPE_REQUEST]范围 存取变量.
     * @param [key] 键
     * @param [callback] 回调
     * @return [T]
     * @author tangli
     * @date 2023/10/24 19:28
     * @since 1.0.0
     */
    @JvmStatic
    public fun <T : Any> ServletRequestAttributes.getOrPut(
        key: String,
        callback: java.util.function.Supplier<T>,
    ): T =
        getAttribute(key, SCOPE_REQUEST)
            .asTo<T>()
            .ifNull(
                callback.get().apply {
                    setAttribute(key, this, SCOPE_REQUEST)
                }
            )

    /**
     * server.servlet.context-path
     */
    @get:JvmName("contextPath")
    @JvmStatic
    public val contextPath: String by SpringContexts.Env.getPropertyByLazy("server.servlet.context-path", "")

    private val webProperties: WebProperties by SpringContexts.getBeanByLazy()

    private val logger = LoggerFactory.getLogger(WebContext::class.java)

    @get:JvmSynthetic
    internal val responseWrapExcludePatterns by lazy(LazyThreadSafetyMode.PUBLICATION) {
        val set =
            setOf(
                *excludePathPatterns(contextPath).toTypedArray(),
                *webProperties
                    .wrapResponseExcludePatterns
                    .map { sanitizedPath("$contextPath/$it") }
                    .toTypedArray()
            )
        logger.info("Response Wrap Exclude Pattern are: $set")
        set
    }

    private const val SWAGGER_UI_PATH: String = "springdoc.swagger-ui.path"
    private const val SWAGGER_UI_PATH_VALUE: String = "/swagger-ui.html"

    private const val SWAGGER_UI_CONFIG: String = "springdoc.swagger-ui.config-url"
    private const val SWAGGER_UI_CONFIG_VALUE: String = "/v3/api-docs/swagger-config"

    private const val SWAGGER_UI_OAUTH2_REDIRECT_URL: String = "springdoc.swagger-ui.oauth2-redirect-url"
    private const val SWAGGER_UI_OAUTH2_REDIRECT_URL_VALUE: String = "/swagger-ui/oauth2-redirect.html"

    private const val SPRINGDOC_API_DOCS_PATH: String = "springdoc.api-docs.path"
    private const val SPRINGDOC_API_DOCS_PATH_VALUE: String = "/v3/api-docs"

    /**
     * 排除路径模式.
     *
     * 全局框架处理层面白名单, 比如不经过全局响应结构包装, 不记录请求日志的 url.
     *
     * 一般默认包含 文档地址, 及对应的静态文件地址.
     *
     * @param [prefix] 前缀
     * @return [Set<String>]
     * @author tangli
     * @date 2024/02/07 09:27
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun excludePathPatterns(prefix: String = ""): Set<String> {
        val actuatorBasePath = SpringContexts.Env.getProperty("management.endpoints.web.base-path", "/actuator")
        val actuatorPrefix = sanitizedPath("$prefix/$actuatorBasePath")
        val errorPath =
            SpringContexts.Env.getProperty(
                "server.error.path",
                SpringContexts.Env.getProperty("error.path", "/error")
            )
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
            sanitizedPath(SpringContexts.Env.getProperty(SWAGGER_UI_PATH, SWAGGER_UI_PATH_VALUE)),
            sanitizedPath(SpringContexts.Env.getProperty(SWAGGER_UI_CONFIG, SWAGGER_UI_CONFIG_VALUE)),
            sanitizedPath(
                SpringContexts.Env.getProperty(SWAGGER_UI_OAUTH2_REDIRECT_URL, SWAGGER_UI_OAUTH2_REDIRECT_URL_VALUE)
            ),
            sanitizedPath(SpringContexts.Env.getProperty(SPRINGDOC_API_DOCS_PATH, SPRINGDOC_API_DOCS_PATH_VALUE))
        )
    }
}
