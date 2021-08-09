package com.tony.dto.req

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@ApiModel("登录请求")
data class UserLoginReq(

    @ApiModelProperty("用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String?,

    @ApiModelProperty("密码", required = true)
    @get:NotBlank(message = "请输入密码")
    val pwd: String?,

)
