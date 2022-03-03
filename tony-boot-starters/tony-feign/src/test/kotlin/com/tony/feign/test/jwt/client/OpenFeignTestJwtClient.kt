package com.tony.feign.test.jwt.client

import com.tony.feign.test.jwt.dto.LoginReq
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "openFeignTestJwtClient", url = "http://localhost:8080")
interface OpenFeignTestJwtClient {

    @PostMapping("/test/login")
    fun testLogin(@RequestBody req: LoginReq): String
}
