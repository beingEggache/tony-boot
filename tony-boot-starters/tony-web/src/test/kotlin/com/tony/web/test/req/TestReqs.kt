package com.tony.web.test.req

import com.tony.enums.validate.SimpleIntEnum
import com.tony.enums.validate.SimpleStringEnum
import javax.validation.constraints.NotBlank

data class TestReq(
    @get:NotBlank(message = "请输入姓名")
    val name: String? = null,
    @get:NotBlank(message = "请输入年龄")
    val age: Int? = null,
    @get:SimpleIntEnum(enums = [1])
    val testIntEnum: TestIntEnum? = null,
    @get:SimpleStringEnum(enums = ["2"])
    val testStringEnum: TestStringEnum? = null
)
