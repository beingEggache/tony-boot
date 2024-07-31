package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * EmployeeDetailReq is
 * @author tangli
 * @date 2024/07/09 10:31
 * @since 1.0.0
 */
@Schema(description = "用户详情请求")
data class EmployeeDetailReq(
    @Schema(description = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val employeeId: String = "",
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
