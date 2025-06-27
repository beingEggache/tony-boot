package tony.demo.sys.dto.resp

import com.fasterxml.jackson.annotation.JsonRawValue
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 字典响应
 * @author tangli
 * @date 2024/07/26 11:47
 */
@Schema(description = "字典响应")
data class DictResp(
    @param:Schema(description = "id")
    val dictId: String = "",
    @param:Schema(description = "字典类型id")
    val dictTypeId: String = "",
    @param:Schema(description = "标签")
    val dictName: String = "",
    @param:Schema(description = "值")
    val dictValue: String = "",
    @param:Schema(description = "编码")
    val dictCode: String = "",
    @JsonRawValue
    @param:Schema(description = "meta")
    val dictMeta: String = "",
    @param:Schema(description = "内置")
    val buildIn: Boolean?,
    @param:Schema(description = "排序")
    val sort: Int,
    @param:Schema(description = "备注")
    val remark: String = "",
    @param:Schema(description = "创建时间")
    val createTime: LocalDateTime,
)
