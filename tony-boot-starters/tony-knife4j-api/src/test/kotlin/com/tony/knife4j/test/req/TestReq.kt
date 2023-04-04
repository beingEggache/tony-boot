package com.tony.knife4j.test.req

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "测试请求体")
data class TestReq(
    @Schema(description = "姓名", defaultValue = "aloha", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 30)
    val name: String,
    @Schema(description = "年龄", defaultValue = "16", maximum = "120", minimum = "0")
    val age: Int,
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    val testReadOnly: Any?,
)
