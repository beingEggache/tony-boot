package com.tony.test.feign.module.unwrap.client

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.test.feign.module.unwrap.api.FeignTestUnwrapApi
import org.springframework.cloud.openfeign.FeignClient

@FeignUnwrapResponse
@FeignClient(name = "feignTestWithUnwrapClient", url = "http://localhost:9090")
interface FeignTestWithUnwrapClient : FeignTestUnwrapApi

@FeignClient(name = "feignTestWithoutUnwrapClient", url = "http://localhost:9090")
interface FeignTestWithoutUnwrapClient : FeignTestUnwrapApi
