package com.tony.knife4j.test.req

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Schema(description = "测试请求体")
data class TestReq(
    @Schema(
        description = "姓名",
        defaultValue = "aloha",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 30,
        minLength = 1
    )
    val name: String,
    @Schema(description = "年龄", defaultValue = "16", maximum = "120", minimum = "0")
    val age: Int,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(pattern = "yyyy-MM-dd")
    val birthDay: LocalDate,
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    val testReadOnly: Any?,
)
