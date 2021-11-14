package com.tony.dto.resp

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

import java.time.LocalDateTime

/**
 *
 * @author tangli
 * @since 2020-11-14 15:18
 */
@ApiModel("用户响应")
data class UserResp(

    @ApiModelProperty("用户Id")
    val userId: String?,

    @ApiModelProperty("用户登录名")
    val userName: String?,

    @ApiModelProperty("用户真实姓名")
    val realName: String?,

    @ApiModelProperty("手机号")
    val mobile: String?,

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createTime: LocalDateTime?,

    @ApiModelProperty("备注")
    val remark: String?,

    @ApiModelProperty("用户状态：1:启用，0:禁用。 可根据需求扩展")
    val states: Int?

)
