package com.tony.test

import com.tony.sys.dao.ModuleDao
import jakarta.annotation.Resource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 *
 * @author tangli
 * @date 2020-11-04 16:54
 */
@SpringBootTest(classes = [TestMonoApiWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ApiModuleInit {


    @Resource
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    @Resource
    lateinit var moduleDao: ModuleDao

//    @Test
//    fun initApiModule() {
//
//        requestMappingHandlerMapping.handlerMethods.filterValues {
//            it.hasMethodAnnotation(ApiOperation::class.java) &&
//            !it.hasMethodAnnotation(NoPermissionCheck::class.java)
//        }.map { (requestMappingInfo, handlerMethod) ->
//
//            val apiOperation = handlerMethod.getMethodAnnotation(ApiOperation::class.java)
//            val moduleName = apiOperation?.value
//                ?: throw ApiException("${handlerMethod.shortLogMessage} apiOperation value null")
//            val moduleId = requestMappingInfo.name ?: handlerMethod.shortLogMessage
//            val moduleGroup = apiOperation.tags.sortedArray().joinToString(",")
//            val remark = apiOperation.notes.defaultIfBlank()
//            val moduleValue = "${requestMappingInfo.methodsCondition.methods.first()} " +
//                requestMappingInfo.patternValues.first()
//            Module().apply {
//                this.moduleId = moduleId
//                this.appId = WebApp.appId
//                this.moduleName = moduleName
//                this.moduleValue = moduleValue
//                this.moduleType = ModuleType.API
//                this.moduleGroup = moduleGroup
//                this.remark = remark
//            }
//        }.forEach {
//            println(it.toJsonString())
//        }
//
//    }
}
