package com.tony.dto.req

import com.tony.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * 新增部门请求
 * @author tangli
 * @date 2024/07/02 11:00
 * @since 1.0.0
 */
@Schema(description = "更新部门请求")
data class DeptUpdateReq(
    @Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择")
    val deptId: String = "",
    @Schema(description = "名称", required = true)
    @get:NotBlank(message = "请输入名称")
    val deptName: String = "",
    @Schema(description = "上级部门id")
    val parentDeptId: String = "",
    @Schema(description = "排序", required = true)
    val sort: Int = -1,
    @NotNull
    @Schema(description = "状态", required = true)
    val enabled: Boolean?,
    @Schema(description = "备注")
    val remarks: String = "",
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
