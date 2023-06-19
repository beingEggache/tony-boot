package com.tony.feign.test.unwrap.client

import com.tony.feign.test.unwrap.api.OpenFeignTestApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "openFeignTestSignatureClient", url = "http://localhost:8080")
interface OpenFeignTestUnwrapClient : OpenFeignTestApi
