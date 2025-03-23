package com.tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 员工重置密码需求
 * @author tangli
 * @date 2025/03/23 22:17
 * @since 1.0.0
 */
@Schema(description = "重置用户密码请求")
data class EmployeeResetPwdReq(
    @param:Schema(description = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val employeeId: String = "",
)
