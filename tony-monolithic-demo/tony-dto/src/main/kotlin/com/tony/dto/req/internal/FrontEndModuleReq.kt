package com.tony.dto.req.internal

import com.tony.dto.enums.ModuleType
import com.tony.enums.validate.SimpleIntEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 *
 * @author tangli
 * @date 2020-11-06 9:50
 */
@Schema(description = "前端 模块/权限 请求")
data class FrontEndModuleReq(
    @Schema(description = "模块/权限Id", required = true)
    @get:NotBlank(message = "请输入模块/权限Id")
    val moduleId: String,
    @Schema(description = "模块/权限名称", required = true)
    @get:NotBlank(message = "请输入模块/权限名称")
    val moduleName: String,
    @Schema(description = "模块/权限值（URL、路由、控件Id）")
    val moduleValue: String? = "",
    @Schema(description = "模块/权限分组")
    val moduleGroup: String = "",
    @Schema(description = "模块/权限说明")
    val remark: String = "",
    @Schema(description = "模块/权限类型", allowableValues = ["2", "3"], required = true)
    @get:SimpleIntEnum(enums = [2, 3], "只能提交前端类型模块")
    @get:NotNull(message = "请输入模块/权限类型")
    val moduleType: ModuleType?,
)
