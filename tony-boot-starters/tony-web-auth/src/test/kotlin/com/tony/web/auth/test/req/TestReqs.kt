package com.tony.web.auth.test.req

import com.tony.enums.validate.SimpleIntEnum
import com.tony.enums.validate.SimpleStringEnum
import com.tony.web.test.req.TestIntEnum
import com.tony.web.test.req.TestStringEnum
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class TestReq(
    @get:NotBlank(message = "请输入姓名")
    val name: String? = null,
    @get:NotNull(message = "请输入年龄")
    val age: Int? = null,
    @get:SimpleIntEnum(enums = [1])
    val testIntEnum: TestIntEnum? = null,
    @get:SimpleStringEnum(enums = ["2"])
    val testStringEnum: TestStringEnum? = null
)

@Schema(description = "测试登录请求")
data class TestLoginReq(
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:NotBlank(message = "请输入用户名")
    val name: String? = null
)

@Schema(description = "测试pattern验证")
data class TestPatternReq(
    @get:Pattern(regexp = "1[0-9]{10}", message = "手机号格式不正确")
    val mobile: String?
)
