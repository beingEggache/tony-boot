package com.tony.test.feign.module.jwt

import com.tony.annotation.EnableTonyBoot
import com.tony.test.feign.config.FeignTestConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import

@Import(FeignTestConfig::class)
@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class FeignJwtTestApp
