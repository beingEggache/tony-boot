package com.tony.dto.req

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 员工查询
 * @author tangli
 * @date 2024/07/02 11:00
 * @since 1.0.0
 */
@Schema(description = "员工查询")
data class EmployeeQuery(
    @Schema(description = "用户名")
    val account: String = "",
    @Schema(description = "真实姓名")
    val realName: String = "",
    @Schema(description = "手机号")
    val employeeMobile: String = "",
    @Schema(description = "部门id")
    val deptId: String = "",
    @Schema(description = "状态")
    val enabled: Boolean? = null,
)
