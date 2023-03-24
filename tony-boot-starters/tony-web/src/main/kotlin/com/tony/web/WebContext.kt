package com.tony.web

import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.Env
import com.tony.exception.BaseException
import com.tony.utils.asTo
import com.tony.utils.defaultIfBlank
import com.tony.utils.returnIfNull
import com.tony.web.utils.headers
import com.tony.web.utils.origin
import com.tony.web.utils.remoteIp
import com.tony.web.utils.url
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.ServletWebRequest
import java.net.URL
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("unused")
public object WebContext {

    @JvmStatic
    public val isWeb: Boolean
        get() = RequestContextHolder.getRequestAttributes() != null

    @JvmStatic
    public val current: ServletRequestAttributes
        get() = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

    @JvmStatic
    @JvmSynthetic
    public fun <T : Any> ServletRequestAttributes.getOrPut(
        key: String,
        scope: Int = SCOPE_REQUEST,
        callback: () -> T,
    ): T = getAttribute(key, scope).asTo() ?: callback().apply {
        setAttribute(key, this, scope)
    }

    @JvmStatic
    public val contextPath: String
        get() = Env.getProperty("server.servlet.context-path", "")

    @JvmStatic
    public val headers: Map<String, String>
        get() = request.headers

    @JvmStatic
    public fun getHeader(name: String?): String =
        headers[name].defaultIfBlank()

    @JvmStatic
    public val origin: String
        get() = request.origin

    @JvmStatic
    public val remoteIP: String
        get() = request.remoteIp

    @JvmStatic
    public val request: HttpServletRequest
        get() = current.request

    @JvmStatic
    public val url: URL
        get() = request.url

    @JvmSynthetic
    public fun BaseException.toResponse(): ApiResult<Map<Any?, Any?>> =
        ApiResult(EMPTY_RESULT, code, message.defaultIfBlank())

    internal val response: HttpServletResponse?
        @JvmSynthetic
        get() = current.response

    private val errorAttributeOptions = ErrorAttributeOptions.of(Include.MESSAGE)

    internal val error: String
        @JvmSynthetic
        get() = errorAttributes["error"].asTo() ?: ""

    internal val errorMessage: String
        @JvmSynthetic
        get() = errorAttributes["message"].asTo() ?: ""

    internal val httpStatus: Int
        @JvmSynthetic
        get() = errorAttributes["status"] as Int

    private val errorAttributes
        get() = current
            .getAttribute("errorAttribute", SCOPE_REQUEST)
            .asTo<Map<String, Any?>>()
            .returnIfNull {
                val errorAttributes = WebApp.errorAttributes
                    .getErrorAttributes(ServletWebRequest(request), errorAttributeOptions)
                current.setAttribute("errorAttribute", errorAttributes, SCOPE_REQUEST)
                errorAttributes
            }
}
