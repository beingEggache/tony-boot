package com.tony.feign.test.signature.client

import com.tony.ApiResult
import com.tony.feign.test.signature.dto.Person
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "openFeignTestSignatureClient", url = "http://localhost:8080")
interface OpenFeignTestSignatureClient {

    @PostMapping(
        "/test-url-post",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        headers = ["X-Header-Process=byHeaderRequestProcessor"]
    )
    fun test2(@RequestBody person: Map<String, *>): ApiResult<Any>

    @PostMapping("/test/test-json-post", headers = ["X-Header-Process=byHeaderRequestProcessor"])
    fun testSignature(@RequestBody person: Person): ApiResult<Any>
}
