package com.tony.feign.test.jwt

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestJwtApp

const val secret = "saoidadsio3jsdfn12323"

