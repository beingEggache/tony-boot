package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import com.tony.demo.validator.annotation.Mobile
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 新增用户请求
 * @author tangli
 * @date 2024/07/02 11:00
 * @since 1.0.0
 */
@Schema(description = "新增用户请求")
data class EmployeeAddReq(
    @get:NotBlank(message = "请输入用户名")
    @param:Schema(description = "用户名", required = true)
    val account: String = "",
    @get:NotBlank(message = "请输入姓名")
    @param:Schema(description = "姓名", required = true)
    val realName: String = "",
    @get:NotBlank(message = "请输入手机号")
    @get:Mobile
    @param:Schema(description = "手机号", required = true)
    val employeeMobile: String = "",
    @param:Schema(description = "部门")
    val deptIds: Set<String> = setOf(),
    @param:Schema(description = "备注")
    val remark: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
