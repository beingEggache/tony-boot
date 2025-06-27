package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import tony.demo.annotation.TenantIdInject

/**
 * 角色查询
 * @author tangli
 * @date 2024/07/03 13:28
 */
@Schema(description = "角色查询")
data class RoleQuery(
    val roleName: String = "",
    @param:Schema(description = "状态")
    val enabled: Boolean? = null,
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
