package com.tony.dto.resp

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @author tangli
 * @since 2020-11-14 13:18
 */
@Schema(description = "角色响应")
data class RoleResp(
    @Schema(description = "角色ID", required = true)
    val roleId: String?,

    @Schema(description = "角色名", required = true)
    val roleName: String?,

    @Schema(description = "备注")
    val remark: String?,
)
