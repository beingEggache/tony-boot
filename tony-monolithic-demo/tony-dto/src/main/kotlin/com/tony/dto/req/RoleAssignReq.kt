package com.tony.dto.req

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

import javax.validation.constraints.NotEmpty

/**
 *
 * @author tangli
 * @since 2020-11-14 13:50
 */
@ApiModel("分配角色请求")
data class RoleAssignReq(

    @get:NotEmpty(message = "请选择用户")
    @ApiModelProperty("用户ID", required = true)
    val userIdList: List<String> = listOf(),

    @get:NotEmpty(message = "请选择角色")
    @ApiModelProperty("用户ID", required = true)
    val roleIdList: List<String> = listOf()
)
