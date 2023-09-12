package com.tony.dto.resp

import com.tony.dto.enums.ModuleType
import com.tony.dto.trait.TreeLike
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @author Tang Li
 * @date 2020-11-14 16:24
 */
@Schema(description = "模块/权限")
data class ModuleResp(

    @Schema(description = "模块/权限Id")
    val moduleId: String,

    @Schema(description = "模块/权限名称")
    val moduleName: String,

    @Schema(description = "模块/权限值（URL、路由、控件Id）")
    val moduleValue: String,

    @Schema(description = "模块/权限类型")
    val moduleType: ModuleType?,

    @Schema(description = "模块/权限分组")
    val moduleGroup: String?,
) : TreeLike<ModuleResp> {

    override val code: String
        get() = moduleId

    @Schema(description = "子路由")
    override var children: List<ModuleResp>? = listOf()

    override val order: Int
        get() = 0
}
