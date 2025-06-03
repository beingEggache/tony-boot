package tony.mono.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


/**
 * 用户登录请求
 *
 * @author tangli
 * @date 2025/03/23 22:26
 */
@Schema(description = "登录请求")
@Getter
@Setter
public class UserLoginReq {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请输入用户名")
    private String userName;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请输入密码")
    private String pwd;
}
