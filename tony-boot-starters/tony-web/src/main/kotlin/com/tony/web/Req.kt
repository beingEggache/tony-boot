package com.tony.web

import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid
import javax.validation.constraints.Positive

data class PageReq(

    @get:Positive(message = "页码请输入正整数")
    @RequestParam(required = false, defaultValue = "1")
    val page: Long = 1,

    @get:Positive(message = "每页数据量请输入正整数")
    @RequestParam(required = false, defaultValue = "10")
    val size: Long = 10
)

data class ListReq<E>(@get:Valid val items: List<E>)
