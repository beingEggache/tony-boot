package com.tony.sys.dto.req

import com.tony.annotation.AppIdInject
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 新增字典类型请求
 * @author tangli
 * @date 2024/07/26 11:47
 * @since 1.0.0
 */
@Schema(description = "新增字典类型请求")
data class DictTypeAddReq(
    @Schema(description = "上级id")
    val parentDictTypeId: String = "",
    @Schema(description = "字典类型编码", required = true)
    @get:NotBlank(message = "请输入字典类型编码")
    val dictTypeCode: String = "",
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
