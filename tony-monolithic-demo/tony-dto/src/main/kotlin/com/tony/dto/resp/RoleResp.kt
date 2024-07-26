package com.tony.dto.resp

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 * @author tangli
 * @date 2020-11-14 13:18
 */
@Schema(description = "角色响应")
data class RoleResp(
    @Schema(description = "id")
    val roleId: String,
    @Schema(description = "名称")
    val roleName: String,
    @Schema(description = "排序")
    val sort: Int,
    @Schema(description = "备注")
    val remark: String?,
    @Schema(description = "状态：1-启用，0-禁用")
    val enabled: Boolean? = null,
    @Schema(description = "创建时间")
    val createTime: LocalDateTime,
)
