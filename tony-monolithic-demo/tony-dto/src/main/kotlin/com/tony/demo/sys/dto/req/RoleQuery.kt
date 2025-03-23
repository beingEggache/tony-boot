package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
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
    @param:Schema(description = "状态")
    val enabled: Boolean? = null,
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
