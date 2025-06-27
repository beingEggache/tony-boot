package tony.demo.sys.dto.resp

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import tony.demo.trait.ForestLike

/**
 * 字典类型响应
 * @author tangli
 * @date 2024/07/26 11:47
 */
@Schema(description = "字典类型响应")
data class DictTypeResp(
    @param:Schema(description = "id")
    val dictTypeId: String = "",
    @param:Schema(description = "字典类型编码")
    val dictTypeCode: String = "",
    @param:Schema(description = "上级id")
    val parentDictTypeId: String = "",
    @param:Schema(description = "字典类型编码序列")
    val dictTypeCodeSeq: String = "",
    @param:Schema(description = "名称")
    val dictTypeName: String = "",
    @get:JsonIgnore(false)
    @param:Schema(description = "排序")
    override val sort: Int,
    @param:Schema(description = "备注")
    val remark: String = "",
    @param:Schema(description = "创建时间")
    val createTime: LocalDateTime,
) : ForestLike<DictTypeResp> {
    override val code: String
        get() = dictTypeCodeSeq

    override var children: MutableList<DictTypeResp> = mutableListOf()

    val title = dictTypeName
}
