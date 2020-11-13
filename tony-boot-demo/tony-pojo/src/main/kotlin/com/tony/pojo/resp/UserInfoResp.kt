package com.tony.pojo.resp

import com.tony.pojo.enums.ModuleType
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

@ApiModel("模块/权限")
data class ModuleResp(

    @ApiModelProperty("模块/权限Id")
    val moduleId: String,

    @ApiModelProperty("模块/权限名称")
    val moduleName: String,

    @ApiModelProperty("模块/权限值（URL、路由、控件Id）")
    val moduleValue: String,

    @ApiModelProperty("模块/权限类型")
    val moduleType: ModuleType?,

    @ApiModelProperty("子路由")
    var children: List<ModuleResp> = listOf(),

)



