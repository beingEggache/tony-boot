package com.tony.test.feign.module.targeter

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.request.GlobalRequestInterceptorProvider
import com.tony.feign.interceptor.request.UseRequestProcessorsRequestInterceptor
import feign.RequestInterceptor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestTargeterApp {

    @Bean
    fun defaultGlobalRequestInterceptor(): GlobalRequestInterceptorProvider<RequestInterceptor> =
        GlobalRequestInterceptorProvider(UseRequestProcessorsRequestInterceptor())

}


