package com.tony.test.feign.module.signature.client

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.annotation.feign.FeignUseGlobalInterceptor
import com.tony.annotation.feign.RequestProcessors
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.test.feign.config.SignatureRequestProcessor
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.signature.api.FeignSignatureTestApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignUnwrapResponse
@FeignUseGlobalInterceptor
@FeignClient(name = "feignSignatureTestClient", url = "http://localhost:10004")
interface FeignSignatureTestClient : FeignSignatureTestApi {

    @NoLoginCheck
    @PostMapping("/test/boolean")
    override fun boolean(): Boolean

    @RequestProcessors(RequestProcessors.Value(SignatureRequestProcessor::class))
    @PostMapping("/test/test-json-unwrap2")
    override fun person(@RequestBody person: Person): Person

}

