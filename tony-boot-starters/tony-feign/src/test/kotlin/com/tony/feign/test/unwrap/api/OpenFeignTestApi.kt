package com.tony.feign.test.unwrap.api

import com.tony.feign.test.unwrap.dto.Person
import com.tony.web.interceptor.NoLoginCheck
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@NoLoginCheck
interface OpenFeignTestApi {

    @PostMapping("/test/test-json-unwrap1")
    fun map(@RequestBody person: Person): Map<String, *>

    @PostMapping("/test/test-json-unwrap2")
    fun person(@RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-validate")
    fun validate(@Validated @RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-unauthorized")
    fun unauthorizedException(@Validated @RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-exception")
    fun exception(@RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-biz-exception")
    fun bizException(@RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-api-exception")
    fun apiException(@RequestBody person: Person): Person
}
