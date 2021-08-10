package com.tony.dto.resp

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 *
 * @author tangli
 * @since 2020-11-04 14:49
 */

@ApiModel("登录用户信息")
data class UserInfoResp(

    @ApiModelProperty("真实姓名")
    val realName: String?,

    @ApiModelProperty("手机号")
    val mobile: String?,

    @ApiModelProperty("模块/权限")
    val modules: RouteAndComponentModuleResp,

)

/**
 *
 * @author tangli
 * @since 2020-11-04 14:49
 */
@ApiModel("路由和控件")
data class RouteAndComponentModuleResp(

    @ApiModelProperty("路由")
    val routes: List<ModuleResp>,

    @ApiModelProperty("控件")
    val components: List<ModuleResp>

)
