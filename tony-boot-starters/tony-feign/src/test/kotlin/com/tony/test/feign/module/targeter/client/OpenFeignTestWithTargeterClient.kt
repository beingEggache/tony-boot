package com.tony.test.feign.module.targeter.client

import com.tony.annotation.feign.FeignUseGlobalInterceptor
import com.tony.test.feign.module.targeter.api.OpenFeignTestTargeterApi
import org.springframework.cloud.openfeign.FeignClient

@FeignUseGlobalInterceptor
@FeignClient(name = "openFeignTestWithTargeterClient", url = "http://localhost:9090")
interface OpenFeignTestWithTargeterClient : OpenFeignTestTargeterApi

@FeignClient(name = "openFeignTestWithoutTargeterClient", url = "http://localhost:9090")
interface OpenFeignTestWithoutTargeterClient : OpenFeignTestTargeterApi
