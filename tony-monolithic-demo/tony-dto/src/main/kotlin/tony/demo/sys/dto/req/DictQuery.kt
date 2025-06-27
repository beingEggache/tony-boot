package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 字典查询
 * @author tangli
 * @date 2024/07/03 13:28
 */
@Schema(description = "字典查询")
data class DictQuery(
    val dictTypeId: String = "",
    val dictName: String = "",
)
