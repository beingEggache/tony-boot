package com.tony.feign.test.signature.controller

import com.tony.feign.test.signature.dto.Person
import com.tony.annotation.web.auth.NoLoginCheck
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class OpenFeignTestSignatureController {

    @NoLoginCheck
    @PostMapping("/test/test-json-post")
    fun test3(@RequestBody person: Person) = mapOf("test" to true)
}
