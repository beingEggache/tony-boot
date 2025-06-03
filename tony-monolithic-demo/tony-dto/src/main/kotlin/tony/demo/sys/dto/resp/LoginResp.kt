package tony.demo.sys.dto.resp

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * UserLoginResp is
 * @author tangli
 * @date 2023/09/06 19:15
 */
@Schema(description = "登录响应")
data class LoginResp(
    val accessToken: String,
    val expiresAt: LocalDateTime,
)
