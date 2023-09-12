package com.tony.dto.resp

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "登录用户信息")
data class UserInfoResp(

    @Schema(description = "真实姓名")
    val realName: String?,

    @Schema(description = "手机号")
    val mobile: String?,

    @Schema(description = "模块/权限")
    val modules: RouteAndComponentModuleResp,
)

@Schema(description = "路由和控件")
data class RouteAndComponentModuleResp(

    @Schema(description = "路由")
    val routes: List<ModuleResp>,

    @Schema(description = "控件")
    val components: List<ModuleResp>,
)
