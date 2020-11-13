package com.tony.webcore.auth.interceptor

import com.tony.webcore.WebContext
import com.tony.webcore.auth.annotation.NoLoginCheck
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod

@ConditionalOnMissingBean(LoginCheckInterceptor::class)
@Component
internal class DefaultLoginCheckInterceptor : LoginCheckInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.method.getAnnotation(NoLoginCheck::class.java) != null) return true
        WebContext.token
        return true
    }
}
