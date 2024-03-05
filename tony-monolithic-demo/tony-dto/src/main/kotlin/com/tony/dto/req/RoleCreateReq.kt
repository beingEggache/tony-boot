package com.tony.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @date 2020-11-14 11:30
 */
@Schema(description = "创建角色请求")
data class RoleCreateReq(
    @Schema(description = "角色ID", required = true)
    @get:NotBlank(message = "请输入角色ID")
    val roleId: String = "",
    @Schema(description = "角色名", required = true)
    @get:NotBlank(message = "请输入角色名")
    var roleName: String = "",
    @Schema(description = "备注")
    var remark: String? = "",
)
