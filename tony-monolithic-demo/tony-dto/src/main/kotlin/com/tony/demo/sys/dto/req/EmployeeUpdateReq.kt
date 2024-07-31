package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "更新用户请求")
data class EmployeeUpdateReq(
    @Schema(description = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val employeeId: String = "",
    @Schema(description = "真实姓名", required = true)
    @get:NotBlank(message = "请输入真实姓名")
    val realName: String = "",
    @Schema(description = "部门")
    val deptIds: Set<String> = setOf(),
    @Schema(description = "备注")
    val remark: String = "",
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
