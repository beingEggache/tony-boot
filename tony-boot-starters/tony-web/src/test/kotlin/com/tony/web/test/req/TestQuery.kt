package com.tony.web.test.req

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class TestQuery @JvmOverloads constructor(
    @field:Positive(message = "请输入正整数")
    val age: Int = 0,

    @field:NotBlank(message = "请输入姓名")
    val name: String = "",

    @field:NotEmpty(message = "请选择爱好")
    val hobby: List<String> = emptyList()
)
