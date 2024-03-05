package com.tony.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

/**
 *
 * @author tangli
 * @date 2020-11-14 13:50
 */
@Schema(description = "分配权限请求")
data class ModuleAssignReq(
    @get:NotEmpty(message = "请选择模块分组")
    @Schema(description = "模块分组", required = true)
    val moduleGroupList: List<String> = listOf(),
    @get:NotEmpty(message = "请选择角色")
    @Schema(description = "用户ID", required = true)
    val roleIdList: List<String> = listOf(),
)
