package com.tony.feign.test.jwt.api

import com.tony.MonoResult
import com.tony.feign.test.jwt.dto.LoginReq
import com.tony.feign.test.jwt.dto.Person
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface OpenFeignTestJwtApi {

    @PostMapping("/test/login")
    fun login(@RequestBody req: LoginReq): MonoResult<String>

    @PostMapping("/test/after-login")
    fun doAfterLogin(@RequestBody person: Person): Person
}
