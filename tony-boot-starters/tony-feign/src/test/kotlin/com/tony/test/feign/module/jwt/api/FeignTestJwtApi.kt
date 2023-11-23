package com.tony.test.feign.module.jwt.api

import com.tony.MonoResult
import com.tony.test.feign.dto.LoginReq
import com.tony.test.feign.dto.Person
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface FeignTestJwtApi {

    @PostMapping("/test/login")
    fun login(@RequestBody req: LoginReq): MonoResult<String>

    @PostMapping("/test/after-login")
    fun doAfterLogin(@RequestBody person: Person): Person
}
