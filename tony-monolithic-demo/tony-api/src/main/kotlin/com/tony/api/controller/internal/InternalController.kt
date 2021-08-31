package com.tony.api.controller.internal

import com.tony.api.permission.NoPermissionCheck
import com.tony.auth.NoLoginCheck
import com.tony.core.utils.doIf
import com.tony.core.utils.getLogger
import com.tony.db.po.Module
import com.tony.db.service.ModuleService
import com.tony.dto.enums.ModuleType
import com.tony.dto.req.internal.FrontEndModuleReq
import com.tony.webcore.WebApp
import com.tony.webcore.req.ListReq
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import javax.annotation.Resource

/**
 *
 * @author tangli
 * @since 2020-11-06 9:09
 */
@Api(tags = ["内部接口"])
@Validated
@RestController
@Profile(value = ["dev", "qa"])
class InternalController(
    private val moduleService: ModuleService
) {

    private val logger = getLogger()

    @ApiOperation("初始化前端权限数据")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/internal/frontend-modules-init")
    fun initFrontendModules(
        @RequestBody
        @Validated
        req: ListReq<FrontEndModuleReq>
    ) = moduleService.saveModules(
        req.items.map { it.toPo() },
        listOf(ModuleType.ROUTE, ModuleType.COMPONENT),
        WebApp.appId
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

    @Lazy
    @Resource
    lateinit var handlerMapping: RequestMappingHandlerMapping

    @ApiOperation("关闭内部接口")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/internal/close-internal-api")
    fun close() {
        this.javaClass.methods.forEach {
            val annot = AnnotatedElementUtils.findMergedAnnotation(it, RequestMapping::class.java)
            val mappingInfo = RequestMappingInfo
                .paths(annot?.path?.first())
                .methods(annot?.method?.first())
                .doIf(!annot?.params.isNullOrEmpty()) {
                    params(annot?.params?.first())
                }
                .build()
            handlerMapping.unregisterMapping(mappingInfo)
        }
        logger.info("internal api closed")
    }
}
