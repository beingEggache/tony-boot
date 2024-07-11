package com.tony.dto.req

import com.tony.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 角色查询
 * @author tangli
 * @date 2024/07/03 13:28
 * @since 1.0.0
 */
@Schema(description = "角色查询")
data class RoleQuery(
    val roleName: String = "",
    @Schema(description = "状态")
    val enabled: Boolean? = null,
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
