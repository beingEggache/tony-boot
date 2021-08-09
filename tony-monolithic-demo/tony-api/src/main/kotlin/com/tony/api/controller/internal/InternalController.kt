package com.tony.api.controller.internal

import com.tony.api.ApiWebApp
import com.tony.api.permission.NoPermissionCheck
import com.tony.auth.NoLoginCheck
import com.tony.db.po.Module
import com.tony.db.service.ModuleService
import com.tony.dto.enums.ModuleType
import com.tony.dto.req.internal.FrontEndModuleReq
import com.tony.webcore.WebApp
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Profile
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

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

    @ApiOperation("初始化前端权限数据")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/internal/frontend-modules-init")
    fun initFrontendModules(
        @RequestBody
        @Valid
        req: List<FrontEndModuleReq>
    ) =
        moduleService.saveModules(
            req.map { it.toPo() },
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
}
