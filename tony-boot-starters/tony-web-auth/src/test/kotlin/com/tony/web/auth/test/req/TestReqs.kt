package com.tony.web.auth.test.req

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

@Schema(description = "测试登录请求")
data class TestLoginReq(
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:NotBlank(message = "请输入用户名")
    val name: String? = null
)

