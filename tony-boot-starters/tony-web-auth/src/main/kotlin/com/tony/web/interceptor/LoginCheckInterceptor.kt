package com.tony.web.interceptor

/**
 *
 * @author tangli
 * @since 2020-11-04 13:33
 */
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.apiSession
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 登录校验拦截器.
 *
 * @author tangli
 * @since 2023/5/25 15:14
 */
public interface LoginCheckInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.beanType.isAnnotationPresent(NoLoginCheck::class.java)) return true
        if (handler.method.isAnnotationPresent(NoLoginCheck::class.java)) return true
        throw (WebContext.apiSession.unauthorizedException ?: return true)
    }
}

/**
 * 登录校验拦截器. 默认实现.
 *
 * @author tangli
 * @since 2023/5/25 15:15
 */
internal class DefaultLoginCheckInterceptor : LoginCheckInterceptor
