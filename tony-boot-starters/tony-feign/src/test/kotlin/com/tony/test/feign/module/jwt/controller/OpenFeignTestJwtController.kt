package com.tony.test.feign.module.jwt.controller

import com.tony.MonoResult
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.jwt.JwtToken
import com.tony.test.feign.dto.LoginReq
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.jwt.api.OpenFeignTestJwtApi
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class OpenFeignTestJwtController : OpenFeignTestJwtApi {

    @NoLoginCheck
    override fun login(@RequestBody req: LoginReq) =
        MonoResult(JwtToken.gen("userId" to "99efd6bbc03b491191ca3206bd20046f"))

    override fun doAfterLogin(@RequestBody person: Person) = person
}
