package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.TenantIdInject

/**
 * 角色分配模块req
 * @author tangli
 * @date 2024/07/05 16:01
 * @since 1.0.0
 */
@Schema(description = "角色分配模块请求")
data class RoleAssignModulesReq(
    @get:NotBlank(message = "请选择角色")
    @param:Schema(description = "角色Id", required = true)
    val roleId: String = "",
    @param:Schema(description = "模块id集合", required = true)
    val moduleIdList: Set<String> = setOf(),
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
