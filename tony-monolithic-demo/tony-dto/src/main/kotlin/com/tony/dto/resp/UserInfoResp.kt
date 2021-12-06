package com.tony.dto.resp

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @author tangli
 * @since 2020-11-04 14:49
 */

@Schema(title = "登录用户信息")
data class UserInfoResp(

    @Schema(title = "真实姓名")
    val realName: String?,

    @Schema(title = "手机号")
    val mobile: String?,

    @Schema(title = "模块/权限")
    val modules: RouteAndComponentModuleResp
)

/**
 *
 * @author tangli
 * @since 2020-11-04 14:49
 */
@Schema(title = "路由和控件")
data class RouteAndComponentModuleResp(

    @Schema(title = "路由")
    val routes: List<ModuleResp>,

    @Schema(title = "控件")
    val components: List<ModuleResp>
)
