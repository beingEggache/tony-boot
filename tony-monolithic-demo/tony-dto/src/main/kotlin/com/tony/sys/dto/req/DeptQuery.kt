package com.tony.sys.dto.req

import com.tony.annotation.TenantIdInject
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 部门查询
 * @author tangli
 * @date 2024/07/03 13:28
 * @since 1.0.0
 */
@Schema(description = "部门查询")
data class DeptQuery(
    val deptName: String = "",
    @Schema(description = "状态")
    val enabled: Boolean? = null,
    val excludeDeptIds: Set<String> = emptySet(),
    @get:TenantIdInject
    @Schema(hidden = true)
    val tenantId: String = "",
)
