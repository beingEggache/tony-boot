package com.tony.test.feign.module.unwrap.client

import com.tony.test.feign.module.unwrap.api.OpenFeignUnwrapTestApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "openFeignTestUnwrapClient", url = "http://localhost:9090")
interface OpenFeignTestUnwrapClient : OpenFeignUnwrapTestApi
