package com.tony.openfeign.test.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class TestController {

    @PostMapping("/test/test-json-post")
    fun test3(@RequestBody person: Person) = mapOf("test" to true)
}

data class Person(
    val array: IntArray?,
    val number: Int?,
    val string: String?,
    val map: Map<String, *>?
)
