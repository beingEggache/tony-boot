/**
 *
 * @author tangli
 * @since 2020-11-04 13:33
 */

package com.tony.web.interceptor

import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.apiSession
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class NoLoginCheck

public interface LoginCheckInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler !is HandlerMethod) return true
        return handleIsLogin(request, response, handler)
    }

    public fun handleIsLogin(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: HandlerMethod
    ): Boolean
}

public class NoopLoginCheckInterceptor : LoginCheckInterceptor {
    override fun handleIsLogin(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: HandlerMethod
    ): Boolean = true
}

internal class DefaultJwtLoginCheckInterceptor : LoginCheckInterceptor {

    override fun handleIsLogin(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: HandlerMethod
    ): Boolean {
        if (handler.method.getAnnotation(NoLoginCheck::class.java) != null) return true
        return WebContext.apiSession.hasLogin()
    }
}
