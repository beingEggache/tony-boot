package com.tony.feign.test.jwt.controller

import com.tony.feign.test.jwt.dto.LoginReq
import com.tony.jwt.JwtToken
import com.tony.web.interceptor.NoLoginCheck
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class OpenFeignTestJwtController {

    @NoLoginCheck
    @PostMapping("/test/login")
    fun test3(@RequestBody req: LoginReq) =
        JwtToken.gen("userId" to "99efd6bbc03b491191ca3206bd20046f")
}
