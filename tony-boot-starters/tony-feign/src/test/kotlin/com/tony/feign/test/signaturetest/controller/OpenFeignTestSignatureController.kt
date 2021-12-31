package com.tony.feign.test.signaturetest.controller

import com.tony.feign.test.dto.Person
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class OpenFeignTestSignatureController {

    @PostMapping("/test/test-json-post")
    fun test3(@RequestBody person: Person) = mapOf("test" to true)
}
