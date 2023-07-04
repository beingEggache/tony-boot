package com.tony.feign.test.jwt.controller

import com.tony.OneResult
import com.tony.feign.test.jwt.api.OpenFeignTestJwtApi
import com.tony.feign.test.jwt.dto.LoginReq
import com.tony.feign.test.jwt.dto.Person
import com.tony.jwt.JwtToken
import com.tony.web.interceptor.NoLoginCheck
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class OpenFeignTestJwtController : OpenFeignTestJwtApi {

    @NoLoginCheck
    override fun login(@RequestBody req: LoginReq) =
        OneResult(JwtToken.gen("userId" to "99efd6bbc03b491191ca3206bd20046f"))

    override fun doAfterLogin(@RequestBody person: Person) = person
}
