package com.tony.dto.resp

import com.tony.dto.enums.ModuleType
import com.tony.dto.trait.ForestLike
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @author tangli
 * @date 2020-11-14 16:24
 */
@Schema(description = "权限响应")
data class ModuleResp(
    @Schema(description = "id")
    val moduleId: String,
    @Schema(description = "上级id")
    val parentModuleId: String,
    @Schema(description = "名称")
    val moduleName: String,
    @Schema(hidden = true)
    val moduleCodeSeq: String,
    @Schema(description = "权限值（URL、路由、控件Id）")
    val moduleValue: String,
    @Schema(description = "权限类型")
    val moduleType: ModuleType,
    @Schema(description = "权限分组")
    val moduleGroup: String?,
) : ForestLike<ModuleResp> {
    override val code: String
        get() = moduleCodeSeq

    @Schema(description = "子路由")
    override var children: MutableList<ModuleResp> = mutableListOf()

    override val sort: Int
        get() = 0
}
