package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.TenantIdInject

/**
 * 用户分配角色请求
 * @author tangli
 * @date 2024/07/05 10:30
 */
@Schema(description = "用户分配角色请求")
data class EmployeeAssignRoleReq(
    @get:NotBlank(message = "请选择用户")
    @param:Schema(description = "用户Id", required = true)
    val employeeId: String = "",
    @param:Schema(description = "角色id集合", required = true)
    val roleIds: Set<String> = setOf(),
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
