package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.UserIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * ResetPwdReq is
 * @author tangli
 * @date 2024/07/10 13:38
 * @since 1.0.0
 */
@Schema(description = "修改密码请求")
data class ChangePwdReq(
    @get:NotBlank(message = "请输入原密码")
    @Schema(description = "原密码", required = true)
    val pwd: String,
    @get:NotBlank(message = "请输入新密码")
    @Schema(description = "新密码", required = true)
    val newPwd: String,
    @get:NotBlank(message = "请重复新密码")
    @Schema(description = "重复密码", required = true)
    val confirmPwd: String,
    @get:UserIdInject
    @Schema(hidden = true)
    val employeeId: String,
)
