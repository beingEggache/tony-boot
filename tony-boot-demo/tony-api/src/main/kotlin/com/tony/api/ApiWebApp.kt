package com.tony.api

import com.tony.api.permission.NoPermissionCheck
import com.tony.api.permission.PermissionInterceptor
import com.tony.core.exception.ApiException
import com.tony.core.utils.defaultIfBlank
import com.tony.db.po.Module
import com.tony.db.service.ModuleService
import com.tony.db.service.UserService
import com.tony.dto.enums.ModuleType
import com.tony.webcore.WebApp
import io.swagger.annotations.ApiOperation
import javax.annotation.Resource
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Lazy
import org.springframework.core.PriorityOrdered
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

fun main(args: Array<String>) {
    runApplication<ApiWebApp>(*args)
}

//@Profile(value = ["!prod"])
//@Component
open class InitApp(
    private val moduleService: ModuleService,
    private val userService: UserService) : CommandLineRunner {

    @Lazy
    @Resource
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    private fun listApiModulesInProject() = requestMappingHandlerMapping.handlerMethods.filterValues {
        it.hasMethodAnnotation(ApiOperation::class.java) &&
            !it.hasMethodAnnotation(NoPermissionCheck::class.java)
    }.map { (requestMappingInfo, handlerMethod) ->

        val apiOperation = handlerMethod.getMethodAnnotation(ApiOperation::class.java)
        val moduleName = apiOperation?.value
        if (moduleName.isNullOrEmpty()) throw ApiException("${handlerMethod.shortLogMessage} apiOperation value null")
        val moduleId = requestMappingInfo.name ?: handlerMethod.shortLogMessage
        val nickname = apiOperation.nickname
        if (nickname.isBlank()) throw ApiException("${handlerMethod.shortLogMessage} apiOperation nickname null")
        val moduleGroup = nickname.split(",").sorted().joinToString(",")
        val moduleDescription = apiOperation.notes.defaultIfBlank()
        val moduleValue = "${requestMappingInfo.methodsCondition.methods.first()} " +
            requestMappingInfo.patternValues.first()
        Module().apply {
            this.moduleId = moduleId
            this.appId = WebApp.appId
            this.moduleName = moduleName
            this.moduleValue = moduleValue
            this.moduleType = ModuleType.API
            this.moduleGroup = moduleGroup
            this.moduleDescription = moduleDescription
        }
    }

    @Transactional
    override fun run(vararg args: String?) {
        moduleService.saveModules(
            this.listApiModulesInProject(),
            listOf(ModuleType.API),
            WebApp.appId)

        userService.initSuperAdmin(WebApp.appId)
    }
}

@SpringBootApplication(scanBasePackages = ["com.tony.**"])
class ApiWebApp(
    private val permissionInterceptor: PermissionInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(permissionInterceptor)
            .excludePathPatterns(*WebApp.ignoreUrlPatterns(true).toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE + 1)
    }

}
