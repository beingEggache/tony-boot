package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 删除字典请求
 * @author tangli
 * @date 2024/07/04 10:52
 * @since 1.0.0
 */
@Schema(description = "删除字典请求")
data class DictDeleteReq(
    @param:Schema(description = "id", required = true)
    @get:NotBlank(message = "请选择")
    val dictId: String = "",
)
