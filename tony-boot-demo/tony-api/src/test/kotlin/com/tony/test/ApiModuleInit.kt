package com.tony.test

import com.tony.api.ApiWebApp
import com.tony.api.permission.NoPermissionCheck
import com.tony.core.exception.ApiException
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.toJsonString
import com.tony.db.dao.ModuleDao
import com.tony.db.po.Module
import com.tony.dto.enums.ModuleType
import com.tony.webcore.WebApp
import io.swagger.annotations.ApiOperation
import javax.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 *
 * @author tangli
 * @since 2020-11-04 16:54
 */
@SpringBootTest(classes = [ApiWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ApiModuleInit {


    @Resource
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    @Resource
    lateinit var moduleDao: ModuleDao

    @Test
    fun initApiModule() {

        requestMappingHandlerMapping.handlerMethods.filterValues {
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
        }.forEach {
            println(it.toJsonString())
        }

    }
}
