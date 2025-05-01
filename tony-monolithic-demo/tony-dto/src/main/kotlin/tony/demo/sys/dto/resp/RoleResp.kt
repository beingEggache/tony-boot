package tony.demo.sys.dto.resp

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 * @author tangli
 * @date 2020-11-14 13:18
 */
@Schema(description = "角色响应")
data class RoleResp(
    @param:Schema(description = "id")
    val roleId: String,
    @param:Schema(description = "名称")
    val roleName: String,
    @param:Schema(description = "备注")
    val remark: String?,
    @param:Schema(description = "状态：1-启用，0-禁用")
    val enabled: Boolean? = null,
    @param:Schema(description = "创建时间")
    val createTime: LocalDateTime,
)
