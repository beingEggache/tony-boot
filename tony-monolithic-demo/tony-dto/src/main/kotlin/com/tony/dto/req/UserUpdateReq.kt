package com.tony.dto.req

import com.tony.validator.annotation.Mobile
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@Schema(title = "更新用户请求")
data class UserUpdateReq(

    @Schema(title = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val userId: String?,

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
)
