package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 新增部门请求
 * @author tangli
 * @date 2024/07/02 11:00
 * @since 1.0.0
 */
@Schema(description = "新增部门请求")
data class DeptAddReq(
    @get:NotBlank(message = "请输入名称")
    @param:Schema(description = "名称", required = true)
    val deptName: String = "",
    @param:Schema(description = "上级id")
    val parentDeptId: String = "",
    @param:Schema(description = "排序", required = true)
    val sort: Int = -1,
    @param:Schema(description = "状态", required = true)
    val enabled: Boolean = true,
    @param:Schema(description = "备注")
    val remark: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
