package com.tony.test.feign.module.file.client

import com.tony.test.feign.module.file.api.FeignFileTestApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "feignFileTestClient", url = "http://localhost:10002")
interface FeignFileTestClient : FeignFileTestApi
