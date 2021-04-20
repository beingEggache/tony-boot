package com.tony.webcore.auth.interceptor

import com.tony.webcore.WebApp
import com.tony.webcore.auth.annotation.NoLoginCheck
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author tangli
 * @since 2020-11-04 13:33
 */
interface LoginCheckInterceptor : HandlerInterceptor

internal class DefaultLoginCheckInterceptor : LoginCheckInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.method.getAnnotation(NoLoginCheck::class.java) != null) return true
        WebApp.apiSession.token
        return true
    }
}
