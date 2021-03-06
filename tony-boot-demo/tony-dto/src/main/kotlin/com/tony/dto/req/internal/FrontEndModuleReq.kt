package com.tony.dto.req.internal

import com.tony.core.enums.validate.SimpleIntEnum
import com.tony.dto.enums.ModuleType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 *
 * @author tangli
 * @since 2020-11-06 9:50
 */
@ApiModel("前端 模块/权限 请求")
data class FrontEndModuleReq(

    @ApiModelProperty("模块/权限Id", required = true)
    @get:NotBlank(message = "请输入模块/权限Id")
    val moduleId: String,

    @ApiModelProperty("模块/权限名称", required = true)
    @get:NotBlank(message = "请输入模块/权限名称")
    val moduleName: String,

    @ApiModelProperty("模块/权限值（URL、路由、控件Id）")
    val moduleValue: String? = "",

    @ApiModelProperty("模块/权限分组")
    val moduleGroup: String = "",

    @ApiModelProperty("模块/权限说明")
    val moduleDescription: String = "",

    @ApiModelProperty("模块/权限类型", allowableValues = "2,3", required = true)
    @get:SimpleIntEnum(enums = [2, 3], "只能提交前端类型模块")
    @get:NotNull(message = "请输入模块/权限类型")
    val moduleType: ModuleType?,
)
