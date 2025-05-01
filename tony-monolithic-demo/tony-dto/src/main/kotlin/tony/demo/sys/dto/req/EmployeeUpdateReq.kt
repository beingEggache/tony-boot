package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.TenantIdInject

/**
 * 员工更新请求
 * @author tangli
 * @date 2025/03/23 22:18
 * @since 1.0.0
 */
@Schema(description = "更新用户请求")
data class EmployeeUpdateReq(
    @param:Schema(description = "用户ID", required = true)
    @get:NotBlank(message = "请选择用户")
    val employeeId: String = "",
    @param:Schema(description = "真实姓名", required = true)
    @get:NotBlank(message = "请输入真实姓名")
    val realName: String = "",
    @param:Schema(description = "部门")
    val deptIds: Set<String> = setOf(),
    @param:Schema(description = "备注")
    val remark: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
