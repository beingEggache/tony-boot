package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.TenantIdInject

/**
 * EmployeeDetailReq is
 * @author tangli
 * @date 2024/07/09 10:31
 * @since 1.0.0
 */
@Schema(description = "用户详情请求")
data class EmployeeDetailReq(
    @param:Schema(description = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val employeeId: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
