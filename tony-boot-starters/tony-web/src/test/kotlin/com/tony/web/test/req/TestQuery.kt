package com.tony.web.test.req

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

data class TestQuery(
    @field:Positive(message = "请输入正整数")
    val age: Int = 0,

    @field:NotBlank(message = "请输入姓名")
    val name: String = "",

    @field:NotEmpty(message = "请选择爱好")
    val hobby: List<@Valid String>
)
