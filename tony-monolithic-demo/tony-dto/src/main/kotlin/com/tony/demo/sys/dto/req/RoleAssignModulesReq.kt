package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 角色分配模块req
 * @author tangli
 * @date 2024/07/05 16:01
 * @since 1.0.0
 */
@Schema(description = "角色分配模块请求")
data class RoleAssignModulesReq(
    @get:NotBlank(message = "请选择角色")
    @Schema(description = "角色Id", required = true)
    val roleId: String = "",
    @Schema(description = "模块id集合", required = true)
    val moduleIdList: Set<String> = setOf(),
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
