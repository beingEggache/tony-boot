package com.tony.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 员工状态切换请求
 * @author tangli
 * @date 2024/07/03 10:53
 * @since 1.0.0
 */
@Schema(description = "员工状态切换请求")
data class EmployeeToggleEnabledReq(
    @Schema(description = "用户名", required = true)
    @get:NotBlank(message = "请选择员工")
    val employeeId: String,
    @Schema(description = "员工启用状态", required = true)
    val enabled: Boolean,
)
