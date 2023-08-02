package com.tony.feign.test.module.targeter

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.GlobalRequestInterceptorProvider
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestTargeterApp {

    @Bean
    fun defaultGlobalRequestInterceptor(): GlobalRequestInterceptorProvider<RequestInterceptor> =
        GlobalRequestInterceptorProvider(DefaultGlobalRequestInterceptor())

}

internal class DefaultGlobalRequestInterceptor : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        template.methodMetadata().method().annotations.forEach { println(it) }
    }
}

