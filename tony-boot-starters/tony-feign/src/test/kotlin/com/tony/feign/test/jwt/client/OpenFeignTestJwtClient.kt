package com.tony.feign.test.jwt.client

import com.tony.feign.test.jwt.api.OpenFeignTestJwtApi
import com.tony.feign.test.jwt.dto.Person
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "openFeignTestJwtClient", url = "http://localhost:8080")
interface OpenFeignTestJwtClient : OpenFeignTestJwtApi {


    @Headers(value = ["X-Header-Process=byHeaderRequestProcessor"])
    @PostMapping("/test/after-login")
    override fun doAfterLogin(person: Person): Person
}
