package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import com.tony.demo.sys.dto.enums.ModuleType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 角色模块查询
 * @author tangli
 * @date 2024/07/08 11:19
 * @since 1.0.0
 */
@Schema(description = "角色模块查询")
data class RoleModuleQuery(
    @get:NotBlank(message = "请选择角色")
    @param:Schema(description = "角色id", required = true)
    val roleId: String,
    @param:Schema(description = "模块类型")
    val moduleTypes: Collection<ModuleType> = listOf(ModuleType.NODE, ModuleType.ROUTE, ModuleType.COMPONENT),
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
