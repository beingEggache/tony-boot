package com.tony.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "重置用户密码请求")
data class EmployeeResetPwdReq(
    @Schema(description = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val employeeId: String = "",
)
