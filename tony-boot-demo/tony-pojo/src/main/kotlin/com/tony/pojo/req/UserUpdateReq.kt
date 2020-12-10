package com.lx.water.platform.pojo.req

import com.lx.water.platform.enums.UserType
import com.lx.water.platform.validator.annotation.Mobile
import com.lx.water.platform.validator.annotation.SimpleIntEnum
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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

    @ApiModelProperty("用户类型 0 普通 1 水投", required = true)
    @get:NotNull(message = "请选择用户类型")
    @get:SimpleIntEnum(message="",enums = [0, 1])
    val userType: UserType?,

    @ApiModelProperty("水库工程ID", required = false)
    val proIdList: List<String>?
)
