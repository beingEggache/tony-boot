package com.tony.dto.resp

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 *
 * @author tangli
 * @since 2020-11-14 13:18
 */
@ApiModel("角色响应")
data class RoleResp(
    @ApiModelProperty("角色ID", required = true)
    val roleId: String?,

    @ApiModelProperty("角色名", required = true)
    val roleName: String?,

    @ApiModelProperty("备注")
    val remark: String?
)
