package com.tony.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

/**
 *
 * @author Tang Li
 * @date 2020-11-14 13:50
 */
@Schema(description = "分配角色请求")
data class RoleAssignReq(
    @get:NotEmpty(message = "请选择用户")
    @Schema(description = "用户ID", required = true)
    val userIdList: List<String> = listOf(),
    @get:NotEmpty(message = "请选择角色")
    @Schema(description = "用户ID", required = true)
    val roleIdList: List<String> = listOf(),
)
