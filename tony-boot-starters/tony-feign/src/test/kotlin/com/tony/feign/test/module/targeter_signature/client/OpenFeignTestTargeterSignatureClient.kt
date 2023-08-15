package com.tony.feign.test.module.targeter_signature.client

import com.tony.annotation.feign.FeignUseGlobalInterceptor
import com.tony.annotation.feign.RequestProcessors
import com.tony.feign.test.dto.Person
import com.tony.feign.test.module.targeter_signature.SignatureRequestProcessor
import com.tony.feign.test.module.targeter_signature.api.OpenFeignTestTargeterSignatureApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignUseGlobalInterceptor
@FeignClient(name = "OpenFeignTestTargeterSignatureClient", url = "http://localhost:9090")
interface OpenFeignTestTargeterSignatureClient : OpenFeignTestTargeterSignatureApi {

    @PostMapping("/test/boolean")
    override fun boolean(): Boolean

    @RequestProcessors(RequestProcessors.Value(SignatureRequestProcessor::class))
    @PostMapping("/test/test-json-unwrap2")
    override fun person(@RequestBody person: Person): Person

}

