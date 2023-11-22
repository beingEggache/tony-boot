package com.tony.test.feign.module.jwt.client

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.jwt.api.OpenFeignTestJwtApi
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignUnwrapResponse
@FeignClient(name = "openFeignTestJwtClient", url = "http://localhost:9090")
interface OpenFeignTestJwtClient : OpenFeignTestJwtApi {


    @Headers(value = ["X-Header-Process=byHeaderRequestProcessor"])
    @PostMapping("/test/after-login")
    override fun doAfterLogin(person: Person): Person
}
