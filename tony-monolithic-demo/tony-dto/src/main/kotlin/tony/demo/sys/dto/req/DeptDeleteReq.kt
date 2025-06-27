package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.TenantIdInject

/**
 * 删除部门请求
 * @author tangli
 * @date 2024/07/04 10:52
 */
@Schema(description = "删除部门请求")
data class DeptDeleteReq(
    @param:Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择部门")
    val deptId: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
