package com.tony.dto.resp

import com.tony.dto.enums.ModuleType
import com.tony.dto.trait.TreeLike
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 *
 * @author tangli
 * @since 2020-11-14 16:24
 */
@ApiModel("模块/权限")
data class ModuleResp(

    @ApiModelProperty("模块/权限Id")
    val moduleId: String,

    @ApiModelProperty("模块/权限名称")
    val moduleName: String,

    @ApiModelProperty("模块/权限值（URL、路由、控件Id）")
    val moduleValue: String,

    @ApiModelProperty("模块/权限类型")
    val moduleType: ModuleType?,

    @ApiModelProperty("模块/权限分组")
    val moduleGroup: String?,

) : TreeLike<ModuleResp> {

    override val code: String
        get() = moduleId

    @ApiModelProperty("子路由")
    override var children: List<ModuleResp>? = listOf()

    override val order: Int
        get() = 0
}
