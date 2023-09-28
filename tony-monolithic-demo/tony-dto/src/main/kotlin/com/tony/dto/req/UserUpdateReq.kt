package com.tony.dto.req

import com.tony.validator.annotation.Mobile
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 *
 * @author Tang Li
 * @date 2020-11-03 14:40
 */
@Schema(description = "更新用户请求")
data class UserUpdateReq(

    @Schema(description = "用户ID", required = true)
    @get:NotNull(message = "请选择用户")
    val userId: Long?,

    @Schema(description = "用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String?,

    @Schema(description = "真实姓名", required = true)
    @get:NotBlank(message = "请输入真实姓名")
    val realName: String?,

    @Schema(description = "手机号", required = true)
    @get:NotBlank(message = "请输入手机号")
    @get:Mobile
    val mobile: String?,
)
