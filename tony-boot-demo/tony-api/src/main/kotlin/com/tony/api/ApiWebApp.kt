package com.tony.api

import com.tony.api.permission.NoPermissionCheck
import com.tony.api.permission.PermissionInterceptor
import com.tony.core.exception.ApiException
import com.tony.core.utils.defaultIfBlank
import com.tony.db.po.Module
import com.tony.pojo.enums.ModuleType
import com.tony.webcore.WebApp
import io.swagger.annotations.ApiOperation
import javax.annotation.Resource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.core.PriorityOrdered
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

fun main(args: Array<String>) {
    runApplication<ApiWebApp>(*args)
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

    @Profile(value = ["dev", "qa"])
    @Component
    companion object InitService {

        lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

        internal fun listApiModulesInProject() = requestMappingHandlerMapping.handlerMethods.filterValues {
            it.hasMethodAnnotation(ApiOperation::class.java) &&
                !it.hasMethodAnnotation(NoPermissionCheck::class.java)
        }.map { (requestMappingInfo, handlerMethod) ->

            val apiOperation = handlerMethod.getMethodAnnotation(ApiOperation::class.java)
            val moduleName = apiOperation?.value
                ?: throw ApiException("${handlerMethod.shortLogMessage} apiOperation value null")
            val moduleId = requestMappingInfo.name ?: handlerMethod.shortLogMessage
            val moduleGroup = apiOperation.tags.sortedArray().joinToString(",")
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

        @Suppress("unused")
        @Lazy
        @Resource
        fun requestMappingHandlerMapping(requestMappingHandlerMapping: RequestMappingHandlerMapping) {
            this.requestMappingHandlerMapping = requestMappingHandlerMapping
        }
    }

}
