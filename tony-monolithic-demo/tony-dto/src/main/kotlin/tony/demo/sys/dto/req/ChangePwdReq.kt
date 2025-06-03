package tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import tony.demo.annotation.UserIdInject

/**
 * ResetPwdReq is
 * @author tangli
 * @date 2024/07/10 13:38
 * @since 1.0.0
 */
@Schema(description = "修改密码请求")
data class ChangePwdReq(
    @get:NotBlank(message = "请输入原密码")
    @param:Schema(description = "原密码", required = true)
    val pwd: String,
    @get:NotBlank(message = "请输入新密码")
    @param:Schema(description = "新密码", required = true)
    val newPwd: String,
    @get:NotBlank(message = "请重复新密码")
    @param:Schema(description = "重复密码", required = true)
    val confirmPwd: String,
    @get:UserIdInject
    @param:Schema(hidden = true)
    val employeeId: String,
)
