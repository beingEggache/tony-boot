package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 登录请求
 * @author tangli
 * @date 2025/03/23 22:18
 * @since 1.0.0
 */
@Schema(description = "登录请求")
data class LoginReq(
    @param:Schema(description = "用户名", required = true)
    @get:NotBlank(message = "请输入用户名")
    val userName: String = "",
    @param:Schema(description = "密码", required = true)
    @get:NotBlank(message = "请输入密码")
    val pwd: String = "",
)
