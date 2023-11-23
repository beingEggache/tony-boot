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

import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.exception.BaseException
import com.tony.utils.asTo
import com.tony.utils.ifNull
import com.tony.utils.ifNullOrBlank
import com.tony.web.utils.headers
import com.tony.web.utils.origin
import com.tony.web.utils.remoteIp
import com.tony.web.utils.url
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.net.URL
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.ServletWebRequest

/**
 * 全局 Web 上下文单例
 *
 * @author Tang Li
 * @date 2023/5/25 10:58
 */
public object WebContext {
    /**
     * 是否web 环境, 单元测试时有用.
     */
    @JvmStatic
    public val isWeb: Boolean
        get() =
            RequestContextHolder.getRequestAttributes() != null

    /**
     * ServletRequestAttributes
     */
    @JvmStatic
    public val current: ServletRequestAttributes
        get() =
            RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

    /**
     * ServletRequestAttributes [scope]范围 存取变量.
     * @param [key] 键
     * @param [scope] 范围
     * @param [callback] 回调
     * @return [T]
     * @author Tang Li
     * @date 2023/10/24 14:28
     * @since 1.0.0
     */
    @JvmStatic
    public fun <T : Any> ServletRequestAttributes.getOrPut(
        key: String,
        scope: Int = SCOPE_REQUEST,
        callback: java.util.function.Supplier<T>,
    ): T =
        getAttribute(key, scope).asTo() ?: callback.get().apply {
            setAttribute(key, this, scope)
        }

    /**
     * ServletRequestAttributes [SCOPE_REQUEST]范围 存取变量.
     * @param [key] 键
     * @param [callback] 回调
     * @return [T]
     * @author Tang Li
     * @date 2023/10/24 14:28
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun <T : Any> ServletRequestAttributes.getOrPut(
        key: String,
        callback: java.util.function.Supplier<T>? = null,
    ): T? =
        getAttribute(key, SCOPE_REQUEST).asTo() ?: callback?.get()?.apply {
            setAttribute(key, this, SCOPE_REQUEST)
        }

    @JvmStatic
    public val headers: Map<String, String>
        get() =
            request.headers

    @JvmStatic
    public fun getHeader(name: String?): String =
        headers[name].ifNullOrBlank()

    @JvmStatic
    public val origin: String
        get() =
            request.origin

    @JvmStatic
    public val remoteIP: String
        get() =
            request.remoteIp

    @JvmStatic
    public val request: HttpServletRequest
        get() =
            current.request

    @JvmStatic
    public val url: URL
        get() =
            request.url

    @JvmSynthetic
    public fun BaseException.toResponse(): ApiResult<*> =
        ApiResult(EMPTY_RESULT, code, message.ifNullOrBlank())

    internal val response: HttpServletResponse?
        @JvmSynthetic
        get() = current.response

    @JvmStatic
    private val errorAttributeOptions =
        ErrorAttributeOptions.of(Include.MESSAGE)

    internal val error: String
        @JvmSynthetic
        get() =
            errorAttributes["error"].asTo() ?: ""

    internal val errorMessage: String
        @JvmSynthetic
        get() =
            errorAttributes["message"].asTo() ?: ""

    internal val httpStatus: Int
        @JvmSynthetic
        get() =
            errorAttributes["status"] as? Int ?: 0

    @Suppress("MemberVisibilityCanBePrivate")
    internal val errorAttributes
        @JvmStatic
        get() =
            current
                .getAttribute("errorAttribute", SCOPE_REQUEST)
                .asTo<Map<String, Any?>>()
                .ifNull {
                    val errorAttributes =
                        WebApp
                            .errorAttributes
                            .getErrorAttributes(ServletWebRequest(request), errorAttributeOptions)
                    current.setAttribute("errorAttribute", errorAttributes, SCOPE_REQUEST)
                    errorAttributes
                }
}
