package com.tony.openfeign.test

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.ByHeaderRequestProcessor
import com.tony.feign.interceptor.ProcessByHeaderInterceptor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import javax.annotation.Resource

@EnableFeignClients
@SpringBootApplication
@EnableTonyBoot
class OpenFeignApp {

    @Bean
    fun processByHeaderInterceptor() = ProcessByHeaderInterceptor("X-Header-Process")

    @Bean
    fun byHeaderRequestProcessor() = MyByHeaderRequestProcessor()
}
