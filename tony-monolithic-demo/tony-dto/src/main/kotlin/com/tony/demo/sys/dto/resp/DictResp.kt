package com.tony.demo.sys.dto.resp

import com.fasterxml.jackson.annotation.JsonRawValue
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 字典响应
 * @author tangli
 * @date 2024/07/26 11:47
 * @since 1.0.0
 */
@Schema(description = "字典响应")
data class DictResp(
    @Schema(description = "id")
    val dictId: String = "",
    @Schema(description = "字典类型id")
    val dictTypeId: String = "",
    @Schema(description = "标签")
    val dictName: String = "",
    @Schema(description = "值")
    val dictValue: String = "",
    @Schema(description = "编码")
    val dictCode: String = "",
    @JsonRawValue
    @Schema(description = "meta")
    val dictMeta: String = "",
    @Schema(description = "内置")
    val buildIn: Boolean?,
    @Schema(description = "排序")
    val sort: Int,
    @Schema(description = "备注")
    val remark: String = "",
    @Schema(description = "创建时间")
    val createTime: LocalDateTime,
)
