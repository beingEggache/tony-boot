package com.tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "登录请求")
data class LoginReq(
    @Schema(description = "用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String = "",
    @Schema(description = "密码", required = true)
    @get:NotBlank(message = "请输入密码")
    val pwd: String = "",
)
