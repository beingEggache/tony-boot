package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.AppIdInject

/**
 * 新增字典类型请求
 * @author tangli
 * @date 2024/07/26 11:47
 */
@Schema(description = "新增字典类型请求")
data class DictTypeAddReq(
    @param:Schema(description = "上级id")
    val parentDictTypeId: String = "",
    @param:Schema(description = "字典类型编码", required = true)
    @get:NotBlank(message = "请输入字典类型编码")
    val dictTypeCode: String = "",
    @param:Schema(description = "名称", required = true)
    @get:NotBlank(message = "请输入名称")
    val dictTypeName: String = "",
    @param:Schema(description = "排序", required = true)
    val sort: Int = -1,
    @param:Schema(description = "备注")
    val remark: String = "",
    @get:AppIdInject
    @param:Schema(hidden = true)
    val appId: String = "",
)
