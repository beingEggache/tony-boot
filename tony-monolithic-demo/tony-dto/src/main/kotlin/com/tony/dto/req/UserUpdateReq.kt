package com.tony.dto.req

import com.tony.validator.annotation.Mobile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@ApiModel("更新用户请求")
data class UserUpdateReq(

    @ApiModelProperty("用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val userId: String?,

    @ApiModelProperty("用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String?,

    @ApiModelProperty("真实姓名", required = true)
    @get:NotBlank(message = "请输入真实姓名")
    val realName: String?,

    @ApiModelProperty("手机号", required = true)
    @get:NotBlank(message = "请输入手机号")
    @get:Mobile
    val mobile: String?,
)
