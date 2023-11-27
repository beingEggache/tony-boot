package com.tony.test.feign.module.unwrap.client

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.test.feign.module.unwrap.api.FeignUnwrapTestApi
import org.springframework.cloud.openfeign.FeignClient

@FeignUnwrapResponse
@FeignClient(name = "feignWithUnwrapTestClient", url = "http://localhost:10005")
interface FeignWithUnwrapTestClient : FeignUnwrapTestApi

@FeignClient(name = "feignWithoutUnwrapTestClient", url = "http://localhost:10005")
interface FeignWithoutUnwrapTestClient : FeignUnwrapTestApi
