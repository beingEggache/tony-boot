package tony.demo.permission

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import tony.demo.MonoApiWebContext.tenantId
import tony.demo.sys.dao.EmployeeDao
import tony.demo.sys.dto.enums.ModuleType
import tony.exception.BizException
import tony.web.WebContext
import tony.web.auth.WebContextExtensions.appId
import tony.web.auth.WebContextExtensions.userId

/**
 *
 * @author tangli
 * @date 2020-11-05 10:06
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NoPermissionCheck

// @Component
// @Profile("prod")
@Suppress("unused")
class DefaultPermissionInterceptor(
    private val employeeDao: EmployeeDao,
) : PermissionInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.method.getAnnotation(NoPermissionCheck::class.java) != null) return true
        val apiModules =
            employeeDao.selectEmployeeModulesByEmployeeIdAndAppId(
                WebContext.userId,
                WebContext.appId,
                WebContext.tenantId,
                listOf(ModuleType.API)
            )
        val moduleId = "${request.method.uppercase()} ${request.requestURI.removePrefix(WebContext.contextPath)}"
        if (!apiModules.any { it.moduleId == moduleId }) throw BizException("未经许可的访问", 40100)
        return true
    }
}

// @Profile("!prod")
@Component
class NoOpPermissionInterceptor : PermissionInterceptor

interface PermissionInterceptor : HandlerInterceptor
