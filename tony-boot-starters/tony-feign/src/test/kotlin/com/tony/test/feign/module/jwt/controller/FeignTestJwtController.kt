package com.tony.test.feign.module.jwt.controller

import com.tony.MonoResult.Companion.ofMonoResult
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.jwt.JwtToken
import com.tony.test.feign.dto.LoginReq
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.jwt.api.FeignJwtTestApi
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class FeignTestJwtController : FeignJwtTestApi {

    @NoLoginCheck
    override fun login(@RequestBody req: LoginReq) =
        JwtToken.gen("userId" to "99efd6bbc03b491191ca3206bd20046f").ofMonoResult()

    override fun doAfterLogin(@RequestBody person: Person) = person
}
