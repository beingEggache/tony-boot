/**
 *
 * @author tangli
 * @since 2020-11-04 13:33
 */

package com.tony.auth

import com.tony.auth.extensions.Extensions.apiSession
import com.tony.webcore.WebApp
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NoLoginCheck

interface LoginCheckInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler !is HandlerMethod) return true
        return handleIsLogin(request, response, handler)
    }

    fun handleIsLogin(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: HandlerMethod
    ): Boolean
}

internal class DefaultLoginCheckInterceptor : LoginCheckInterceptor {

    override fun handleIsLogin(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: HandlerMethod
    ): Boolean {
        if (handler.method.getAnnotation(NoLoginCheck::class.java) != null) return true
        return WebApp.apiSession.hasLogin()
    }
}
