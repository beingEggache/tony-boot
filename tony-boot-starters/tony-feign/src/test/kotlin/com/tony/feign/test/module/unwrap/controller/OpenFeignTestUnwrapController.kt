package com.tony.feign.test.module.unwrap.controller

import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.feign.test.dto.Person
import com.tony.feign.test.module.unwrap.api.OpenFeignUnwrapTestApi
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class OpenFeignTestUnwrapController : OpenFeignUnwrapTestApi {

    @NoLoginCheck
    override fun map(@RequestBody person: Person) = mapOf("test" to true)

    @NoLoginCheck
    override fun person(@RequestBody person: Person) = person

    @NoLoginCheck
    override fun validate(@Validated @RequestBody person: Person): Person = person

    override fun unauthorizedException(person: Person): Person = person

    @NoLoginCheck
    override fun exception(@RequestBody person: Person): Person = throw Exception("go fuck yourself")

    @NoLoginCheck
    override fun bizException(@RequestBody person: Person): Person = throw BizException("go fuck yourself")

    @NoLoginCheck
    override fun apiException(@RequestBody person: Person): Person = throw ApiException("go fuck yourself")
}
