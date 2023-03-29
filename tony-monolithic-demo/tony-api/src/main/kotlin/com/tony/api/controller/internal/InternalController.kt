package com.tony.api.controller.internal

import com.tony.api.permission.NoPermissionCheck
import com.tony.db.po.Module
import com.tony.db.service.ModuleService
import com.tony.dto.enums.ModuleType
import com.tony.dto.req.ListReq
import com.tony.dto.req.internal.FrontEndModuleReq
import com.tony.utils.getLogger
import com.tony.web.WebApp
import com.tony.web.interceptor.NoLoginCheck
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author tangli
 * @since 2020-11-06 9:09
 */
@Tag(name = "内部接口")
@Validated
@RestController
@Profile(value = ["dev", "qa"])
class InternalController(
    private val moduleService: ModuleService,
) {

    private val logger = getLogger()

    @Operation(summary = "初始化前端权限数据")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/internal/frontend-modules-init")
    fun initFrontendModules(
        @RequestBody
        @Validated
        req: ListReq<FrontEndModuleReq>,
    ) = moduleService.saveModules(
        req.items.map { it.toPo() },
        listOf(ModuleType.ROUTE, ModuleType.COMPONENT),
        WebApp.appId,
    )

    private fun FrontEndModuleReq.toPo() = let {
        Module().apply {
            this.moduleId = it.moduleId
            this.appId = WebApp.appId
            this.moduleName = it.moduleName
            this.moduleValue = it.moduleValue
            this.moduleType = it.moduleType
            this.moduleGroup = it.moduleGroup
            this.moduleDescription = it.moduleDescription
        }
    }

/*    @Lazy
    @Resource
    lateinit var handlerMapping: RequestMappingHandlerMapping

    @Operation(summary = "关闭内部接口")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/internal/close-internal-api")
    fun close() {
        this.javaClass.methods.forEach {
            val annot = AnnotatedElementUtils.findMergedAnnotation(it, RequestMapping::class.java)
            val mappingInfo = RequestMappingInfo
                .paths(*(annot?.path ?: arrayOf()))
                .methods(*(annot?.method ?: arrayOf()))
                .build()
            handlerMapping.unregisterMapping(mappingInfo)
        }
        logger.info("internal api closed")
    }*/
}
