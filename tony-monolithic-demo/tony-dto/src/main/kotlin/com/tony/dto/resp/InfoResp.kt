package com.tony.dto.resp

import io.swagger.v3.oas.annotations.media.Schema

/**
 * InfoResp is
 * @author tangli
 * @date 2024/07/02 13:33
 * @since 1.0.0
 */
data class InfoResp(
    @Schema(description = "用户id")
    val employeeId: String,
    @Schema(description = "姓名")
    val realName: String,
    @Schema(description = "手机号")
    val employeeMobile: String,
) {
    @Schema(description = "路由列表")
    var routePaths: List<String> = emptyList()

    @Schema(description = "控件列表")
    var componentCodes: List<String> = emptyList()
}
