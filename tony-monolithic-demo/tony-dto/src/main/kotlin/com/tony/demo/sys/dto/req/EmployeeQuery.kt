package com.tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 员工查询
 * @author tangli
 * @date 2024/07/02 11:00
 * @since 1.0.0
 */
@Schema(description = "员工查询")
data class EmployeeQuery(
    @param:Schema(description = "用户名")
    val account: String = "",
    @param:Schema(description = "真实姓名")
    val realName: String = "",
    @param:Schema(description = "手机号")
    val employeeMobile: String = "",
    @param:Schema(description = "部门id")
    val deptId: String = "",
    @param:Schema(description = "状态")
    val enabled: Boolean? = null,
)
