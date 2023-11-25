package com.tony.test.feign.module.jwt.client

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.annotation.feign.FeignUseGlobalInterceptor
import com.tony.annotation.feign.RequestProcessors
import com.tony.test.feign.module.jwt.AddTokenRequestProcessor
import com.tony.test.feign.module.jwt.api.FeignJwtTestApi
import org.springframework.cloud.openfeign.FeignClient

@RequestProcessors(RequestProcessors.Value(AddTokenRequestProcessor::class))
@FeignUnwrapResponse
@FeignUseGlobalInterceptor
@FeignClient(name = "feignJwtTestClient", url = "http://localhost:9092")
interface FeignJwtTestClient : FeignJwtTestApi
