package com.tony.sys.dto.resp

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.trait.ForestLike
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 字典类型响应
 * @author tangli
 * @date 2024/07/26 11:47
 * @since 1.0.0
 */
@Schema(description = "字典类型响应")
data class DictTypeResp(
    @Schema(description = "id")
    val dictTypeId: String = "",
    @Schema(description = "字典类型编码")
    val dictTypeCode: String = "",
    @Schema(description = "上级id")
    val parentDictTypeId: String = "",
    @Schema(description = "字典类型编码序列")
    val dictTypeCodeSeq: String = "",
    @Schema(description = "名称")
    val dictTypeName: String = "",
    @get:JsonIgnore(false)
    @Schema(description = "排序")
    override val sort: Int,
    @Schema(description = "备注")
    val remark: String = "",
    @Schema(description = "创建时间")
    val createdAt: LocalDateTime,
) : ForestLike<DictTypeResp> {
    override val code: String
        get() = dictTypeCodeSeq

    override var children: MutableList<DictTypeResp> = mutableListOf()

    val title = dictTypeName
}
