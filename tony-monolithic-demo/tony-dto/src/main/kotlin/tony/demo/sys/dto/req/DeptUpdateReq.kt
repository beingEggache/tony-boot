package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import tony.demo.annotation.TenantIdInject

/**
 * 新增部门请求
 * @author tangli
 * @date 2024/07/02 11:00
 */
@Schema(description = "更新部门请求")
data class DeptUpdateReq(
    @param:Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择")
    val deptId: String = "",
    @param:Schema(description = "名称", required = true)
    @get:NotBlank(message = "请输入名称")
    val deptName: String = "",
    @param:Schema(description = "上级部门id")
    val parentDeptId: String = "",
    @param:Schema(description = "排序", required = true)
    val sort: Int = -1,
    @get:NotNull
    @param:Schema(description = "状态", required = true)
    val enabled: Boolean?,
    @param:Schema(description = "备注")
    val remark: String = "",
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
