package com.tony.dto.req

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

import javax.validation.constraints.NotEmpty

/**
 *
 * @author tangli
 * @since 2020-11-14 13:50
 */
@ApiModel("分配权限请求")
data class ModuleAssignReq(

    @get:NotEmpty(message = "请选择模块分组")
    @ApiModelProperty("模块分组", required = true)
    val moduleGroupList: List<String> = listOf(),

    @get:NotEmpty(message = "请选择角色")
    @ApiModelProperty("用户ID", required = true)
    val roleIdList: List<String> = listOf()
)
