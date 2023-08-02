package com.tony.feign.test.module.jwt.client

import com.tony.feign.test.dto.Person
import com.tony.feign.test.module.jwt.api.OpenFeignTestJwtApi
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "openFeignTestJwtClient", url = "http://localhost:9090")
interface OpenFeignTestJwtClient : OpenFeignTestJwtApi {


    @Headers(value = ["X-Header-Process=byHeaderRequestProcessor"])
    @PostMapping("/test/after-login")
    override fun doAfterLogin(person: Person): Person
}
