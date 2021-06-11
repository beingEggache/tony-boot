package com.tony.dto.req

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

/**
 *
 * @author tangli
 * @since 2020-11-14 11:30
 */
@ApiModel("创建角色请求")
data class RoleCreateReq(

    @ApiModelProperty("角色ID", required = true)
    @get:NotBlank(message = "请输入角色ID")
    val roleId: String?,

    @ApiModelProperty("角色名", required = true)
    @get:NotBlank(message = "请输入角色名")
    var roleName: String?,

    @ApiModelProperty("备注")
    var remark: String?
)
