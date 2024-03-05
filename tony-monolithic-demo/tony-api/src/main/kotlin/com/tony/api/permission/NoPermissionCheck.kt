package com.tony.api.permission

import com.tony.db.service.ModuleService
import com.tony.exception.BizException
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.appId
import com.tony.web.WebContextExtensions.userId
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 *
 * @author tangli
 * @date 2020-11-05 10:06
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NoPermissionCheck

@Component
@Profile("prod")
class DefaultPermissionInterceptor(
    private val moduleService: ModuleService,
) : PermissionInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.method.getAnnotation(NoPermissionCheck::class.java) != null) return true
        val apiModules = moduleService.listApiModules(WebContext.userId, WebContext.appId)
        val moduleId = "${request.method.uppercase()} ${request.requestURI.removePrefix(WebContext.contextPath)}"
        if (!apiModules.any { it.moduleId == moduleId }) throw BizException("未经许可的访问", 40100)
        return true
    }
}

@Component
@Profile("!prod")
class NoOpPermissionInterceptor : PermissionInterceptor

interface PermissionInterceptor : HandlerInterceptor
