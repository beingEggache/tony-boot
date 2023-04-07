/**
 *
 * @author tangli
 * @since 2020-11-04 13:33
 */

package com.tony.web.interceptor

import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.apiSession
import com.tony.web.exception.UnauthorizedException
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
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        throw (loginOk(handler) ?: return true)
    }

    public fun loginOk(handler: HandlerMethod): UnauthorizedException? = null
}

public class NoopLoginCheckInterceptor : LoginCheckInterceptor

internal class DefaultJwtLoginCheckInterceptor : LoginCheckInterceptor {

    override fun loginOk(handler: HandlerMethod): UnauthorizedException? {
        if (handler.method.getAnnotation(NoLoginCheck::class.java) != null) return null
        return WebContext.apiSession.loginOk()
    }
}
