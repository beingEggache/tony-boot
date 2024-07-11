package com.tony.dto.req

import com.tony.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * RoleDeleteReq is
 * @author tangli
 * @date 2024/07/04 10:52
 * @since 1.0.0
 */
@Schema(description = "删除角色请求")
data class RoleDeleteReq(
    @get:NotBlank(message = "请选择")
    @Schema(description = "id", required = true)
    val roleId: String = "",
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
