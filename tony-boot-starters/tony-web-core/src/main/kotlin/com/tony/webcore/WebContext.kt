@file:Suppress("MemberVisibilityCanBePrivate")

package com.tony.webcore

import com.tony.core.ApiResult
import com.tony.core.EMPTY_RESULT
import com.tony.core.exception.BaseException
import com.tony.core.utils.asTo
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.doIfNull
import com.tony.webcore.utils.headers
import com.tony.webcore.utils.origin
import com.tony.webcore.utils.remoteIp
import com.tony.webcore.utils.url
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
object WebContext {

    val current: ServletRequestAttributes
        get() = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

    fun <T : Any> ServletRequestAttributes.getOrPut(
        key: String,
        scope: Int = SCOPE_REQUEST,
        callback: () -> T
    ) = getAttribute(key, scope).asTo() ?: callback().apply {
        setAttribute(key, this, scope)
    }

    val contextPath: String
        get() = WebApp.contextPath

    val headers: Map<String, String>
        get() = request.headers

    private val errorAttributeOptions = ErrorAttributeOptions.of(Include.MESSAGE)

    internal val error: String
        get() = errorAttributes["error"].asTo() ?: ""

    internal val httpStatus: Int
        get() = errorAttributes["status"] as Int

    private val errorAttributes
        get() = current.getAttribute("errorAttribute", SCOPE_REQUEST)
            .asTo<Map<String, Any?>>().doIfNull {
                val errorAttributes = WebApp
                    .errorAttributes
                    .getErrorAttributes(ServletWebRequest(request), errorAttributeOptions)
                current.setAttribute("errorAttribute", errorAttributes, SCOPE_REQUEST)
                errorAttributes
            }

    val origin: String
        get() = request.origin

    val remoteIP: String
        get() = request.remoteIp

    val request: HttpServletRequest
        get() = current.request

    internal val response: HttpServletResponse?
        get() = current.response

    val url: URL
        get() = request.url

    fun BaseException.toResponse() =
        ApiResult(EMPTY_RESULT, code, message.defaultIfBlank())
}
