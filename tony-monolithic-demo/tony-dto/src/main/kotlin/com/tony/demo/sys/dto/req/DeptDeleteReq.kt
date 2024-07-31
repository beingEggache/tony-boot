package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 删除部门请求
 * @author tangli
 * @date 2024/07/04 10:52
 * @since 1.0.0
 */
@Schema(description = "删除部门请求")
data class DeptDeleteReq(
    @Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择部门")
    val deptId: String = "",
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
