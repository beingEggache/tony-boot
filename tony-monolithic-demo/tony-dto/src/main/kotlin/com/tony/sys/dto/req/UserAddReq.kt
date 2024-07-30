package com.tony.sys.dto.req

import com.tony.validator.annotation.Mobile
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @date 2020-11-03 14:40
 */
@Schema(description = "新增用户请求")
data class UserAddReq(
    @Schema(description = "用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String = "",
    @Schema(description = "真实姓名", required = true)
    @get:NotBlank(message = "请输入真实姓名")
    val realName: String = "",
    @Schema(description = "手机号", required = true)
    @get:NotBlank(message = "请输入手机号")
    @get:Mobile
    val mobile: String = "",
    @Schema(description = "密码", required = true)
    @get:NotBlank(message = "请输入密码")
    val pwd: String = "",
    @Schema(description = "重复", required = true)
    @get:NotBlank(message = "请重复密码")
    val confirmPwd: String = "",
)
