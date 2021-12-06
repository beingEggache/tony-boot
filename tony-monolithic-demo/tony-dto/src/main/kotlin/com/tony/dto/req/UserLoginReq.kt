package com.tony.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@Schema(title = "登录请求")
data class UserLoginReq(

    @Schema(title = "用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String?,

    @Schema(title = "密码", required = true)
    @get:NotBlank(message = "请输入密码")
    val pwd: String?
)
