package com.tony.test.feign.module.unwrap

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class FeignUnwrapTestApp
