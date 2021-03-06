package com.tony.api.permission

import com.tony.core.exception.BizException
import com.tony.db.service.ModuleService
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 *
 * @author tangli
 * @since 2020-11-05 10:06
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NoPermissionCheck

@Component
@Profile("prod")
class DefaultPermissionInterceptor(
    private val moduleService: ModuleService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.method.getAnnotation(NoPermissionCheck::class.java) != null) return true
        val apiModules = moduleService.listApiModules(WebContext.userId, WebApp.appId)
        val moduleId = "${request.method.toUpperCase()} ${request.requestURI.removePrefix(WebContext.contextPath)}"
        if (!apiModules.any { it.moduleId == moduleId }) throw BizException("未经许可的访问", 40100)
        return true
    }
}

@Component
@Profile("!prod")
class NoOpPermissionInterceptor : PermissionInterceptor

interface PermissionInterceptor : HandlerInterceptor
