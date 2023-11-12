package com.tony.test.feign.module.signature.client

import com.tony.test.feign.dto.Person
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "openFeignTestSignatureClient", url = "http://localhost:9090")
interface OpenFeignTestSignatureClient {

    @PostMapping(
        "/test-url-post",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        headers = ["X-Header-Process=signatureRequestProcessor,printHeaderRequestProcessor"]
    )
    fun test2(@RequestBody person: Map<String, *>): Any

    @PostMapping("/test/test-json-post", headers = ["X-Header-Process=signatureRequestProcessor,printHeaderRequestProcessor"])
    fun testSignature(@RequestBody person: Person): Any
}
