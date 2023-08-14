package com.tony.feign.test.module.targeter_signature.client

import com.tony.annotation.feign.RequestProcessors
import com.tony.annotation.web.feign.FeignUseGlobalInterceptor
import com.tony.feign.interceptor.request.BeanType
import com.tony.feign.test.module.targeter_signature.FeignRequestProcess3
import com.tony.feign.test.module.targeter_signature.api.OpenFeignTestTargeterSignatureApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignUseGlobalInterceptor
@FeignClient(name = "OpenFeignTestTargeterSignatureClient", url = "http://localhost:9090")
interface OpenFeignTestTargeterSignatureClient : OpenFeignTestTargeterSignatureApi {

    @RequestProcessors(
        [
            RequestProcessors.Value(FeignRequestProcess3::class),
            RequestProcessors.Value(name = "feignRequestProcess1", type = BeanType.NAME),
            RequestProcessors.Value(name = "feignRequestProcess2", type = BeanType.NAME),
        ]
    )
    @PostMapping("/test/boolean")
    override fun boolean(): Boolean

}

