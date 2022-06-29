package com.tony.dto.req

import com.tony.validator.annotation.Mobile
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@Schema(title = "新增用户请求")
data class UserCreateReq(

    @Schema(title = "用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String?,

    @Schema(title = "真实姓名", required = true)
    @get:NotBlank(message = "请输入真实姓名")
    val realName: String?,

    @Schema(title = "手机号", required = true)
    @get:NotBlank(message = "请输入手机号")
    @get:Mobile
    val mobile: String?,

    @Schema(title = "密码", required = true)
    @get:NotBlank(message = "请输入密码")
    val pwd: String?,

    @Schema(title = "重复", required = true)
    @get:NotBlank(message = "请重复密码")
    val confirmPwd: String?
)
