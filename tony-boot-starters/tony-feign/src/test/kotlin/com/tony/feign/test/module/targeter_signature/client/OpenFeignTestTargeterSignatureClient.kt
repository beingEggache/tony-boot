package com.tony.feign.test.module.targeter_signature.client

import com.tony.annotation.web.feign.FeignUseGlobalInterceptor
import com.tony.feign.test.module.targeter_signature.FeignRequestProcess3
import com.tony.feign.test.module.targeter_signature.FeignRequestProcessValue
import com.tony.feign.test.module.targeter_signature.FeignRequestProcessValueType
import com.tony.feign.test.module.targeter_signature.FeignRequestProcesses
import com.tony.feign.test.module.targeter_signature.api.OpenFeignTestTargeterSignatureApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignUseGlobalInterceptor
@FeignClient(name = "OpenFeignTestTargeterSignatureClient", url = "http://localhost:9090")
interface OpenFeignTestTargeterSignatureClient : OpenFeignTestTargeterSignatureApi {

    @FeignRequestProcesses(
        [
            FeignRequestProcessValue(clazz = FeignRequestProcess3::class),
            FeignRequestProcessValue(type = FeignRequestProcessValueType.NAME, name = "feignRequestProcess1"),
            FeignRequestProcessValue(type = FeignRequestProcessValueType.NAME, name = "feignRequestProcess2"),
        ]
    )
    @PostMapping("/test/boolean")
    override fun boolean(): Boolean

}

