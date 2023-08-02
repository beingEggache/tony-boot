package com.tony.feign.test.module.targeter.client

import com.tony.feign.test.module.targeter.api.OpenFeignTestApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "openFeignTestClient", url = "http://localhost:9090")
interface OpenFeignTestClient : OpenFeignTestApi
