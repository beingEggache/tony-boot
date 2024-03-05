package com.tony.api.controller.internal

import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.api.permission.NoPermissionCheck
import com.tony.db.service.ModuleService
import com.tony.dto.enums.ModuleType
import com.tony.dto.req.ListReq
import com.tony.dto.req.internal.FrontEndModuleReq
import com.tony.utils.copyTo
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.appId
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
 * @date 2020-11-06 9:09
 */
@Tag(name = "内部接口")
@Validated
@RestController
@Profile(value = ["dev", "qa"])
class InternalController(
    private val moduleService: ModuleService,
) {
    @Operation(summary = "初始化前端权限数据")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/internal/frontend-modules-init")
    fun initFrontendModules(
        @RequestBody
        @Validated
        req: ListReq<FrontEndModuleReq>,
    ) = moduleService.saveModules(
        req.rows.map { it.copyTo() },
        listOf(ModuleType.ROUTE, ModuleType.COMPONENT),
        WebContext.appId
    )
}
