package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import tony.demo.annotation.TenantIdInject

/**
 * 新增角色请求
 * @author tangli
 * @date 2024/07/04 11:07
 * @since 1.0.0
 */
@Schema(description = "更新角色请求")
data class RoleUpdateReq(
    @get:NotBlank(message = "请选择")
    @param:Schema(description = "id", required = true)
    val roleId: String = "",
    /**
     * 角色名
     */
    @get:NotBlank(message = "请输入名称")
    @param:Schema(description = "名称", required = true)
    val roleName: String = "",
    @param:Schema(description = "排序", required = true)
    val sort: Int = -1,
    /**
     * 备注
     */
    @param:Schema(description = "备注")
    val remark: String = "",
    /**
     * 状态：1-启用，0-禁用
     */
    @get:NotNull
    @param:Schema(description = "状态", required = true)
    val enabled: Boolean?,
    /**
     * 租户id
     */
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
)
