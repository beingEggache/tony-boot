package tony.demo.sys.dto.query

import io.swagger.v3.oas.annotations.media.Schema
import tony.demo.annotation.TenantIdInject

/**
 * 部门查询
 * @author tangli
 * @date 2024/07/03 13:28
 */
@Schema(description = "部门查询")
data class DeptQuery(
    val deptName: String = "",
    @param:Schema(description = "状态")
    val enabled: Boolean? = null,
    val excludeDeptIds: Set<String> = emptySet(),
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
