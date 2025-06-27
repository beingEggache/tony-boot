package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.TenantIdInject

/**
 * RoleDeleteReq is
 * @author tangli
 * @date 2024/07/04 10:52
 */
@Schema(description = "删除角色请求")
data class RoleDeleteReq(
    @get:NotBlank(message = "请选择")
    @param:Schema(description = "id", required = true)
    val roleId: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
