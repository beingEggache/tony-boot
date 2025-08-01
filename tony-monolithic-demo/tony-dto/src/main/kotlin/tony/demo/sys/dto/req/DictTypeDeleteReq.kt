package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 删除字典类型请求
 * @author tangli
 * @date 2024/07/04 10:52
 */
@Schema(description = "删除字典类型请求")
data class DictTypeDeleteReq(
    @param:Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择")
    val dictTypeId: String = "",
)
