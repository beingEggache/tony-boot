package tony.demo.sys.dto.resp

import io.swagger.v3.oas.annotations.media.Schema
import tony.core.model.ForestLike
import tony.demo.sys.dto.enums.ModuleType

/**
 *
 * @author tangli
 * @date 2020-11-14 16:24
 */
@Schema(description = "权限响应")
data class ModuleResp(
    @param:Schema(description = "id")
    val moduleId: String,
    @param:Schema(description = "上级id")
    val parentModuleId: String,
    @param:Schema(description = "名称")
    val moduleName: String,
    @param:Schema(hidden = true)
    val moduleCodeSeq: String,
    @param:Schema(description = "权限值（URL、路由、控件Id）")
    val moduleValue: String,
    @param:Schema(description = "权限类型")
    val moduleType: ModuleType,
    @param:Schema(description = "权限分组")
    val moduleGroup: String?,
) : ForestLike<ModuleResp> {
    override val code: String
        get() = moduleCodeSeq

    @get:Schema(description = "子路由")
    override val children: MutableCollection<ModuleResp> = mutableListOf()

    override val sort: Int
        get() = 0
}
