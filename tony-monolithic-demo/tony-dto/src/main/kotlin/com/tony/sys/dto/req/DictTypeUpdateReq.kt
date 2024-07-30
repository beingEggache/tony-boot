package com.tony.sys.dto.req

import com.tony.annotation.AppIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * DictTypeAddReq is
 * @author tangli
 * @date 2024/07/26 11:47
 * @since 1.0.0
 */
@Schema(description = "更新字典类型请求")
data class DictTypeUpdateReq(
    @Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择")
    val dictTypeId: String = "",
    @Schema(description = "上级id")
    val parentDictTypeId: String = "",
    @Schema(description = "名称", required = true)
    @get:NotBlank(message = "请输入名称")
    val dictTypeName: String = "",
    @Schema(description = "排序", required = true)
    val sort: Int = -1,
    @Schema(description = "备注")
    val remark: String = "",
    @get:AppIdInject
    @Schema(hidden = true)
    val appId: String = "",
)
