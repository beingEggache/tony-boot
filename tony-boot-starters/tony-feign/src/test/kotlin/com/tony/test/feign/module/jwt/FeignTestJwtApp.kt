package com.tony.test.feign.module.jwt

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class FeignTestJwtApp

const val secret = "saoidadsio3jsdfn12323"

