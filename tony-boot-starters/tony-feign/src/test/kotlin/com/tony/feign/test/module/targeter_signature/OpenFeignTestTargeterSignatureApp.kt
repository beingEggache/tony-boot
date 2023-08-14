package com.tony.feign.test.module.targeter_signature

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.request.GlobalRequestInterceptorProvider
import com.tony.feign.interceptor.request.RequestProcessor
import com.tony.feign.interceptor.request.UseRequestProcessorsRequestInterceptor
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestTargeterSignatureApp {

    @Bean("feignRequestProcess1")
    fun feignRequestProcess1(): RequestProcessor {
        return RequestProcessor { template ->
            println(1)
            template.header("X-1", "1")
        }
    }

    @Bean("feignRequestProcess2")
    fun feignRequestProcess2(): RequestProcessor {
        return RequestProcessor { template ->
            println(2)
            template.header("X-2", "2")
        }
    }

    @Bean
    fun feignRequestProcess3(): FeignRequestProcess3 {
        return FeignRequestProcess3()
    }

    @Bean
    fun defaultGlobalRequestInterceptor(): GlobalRequestInterceptorProvider<RequestInterceptor> =
        GlobalRequestInterceptorProvider(UseRequestProcessorsRequestInterceptor())

}


class FeignRequestProcess3 : RequestProcessor {
    override operator fun invoke(template: RequestTemplate) {
        println(3)
        template.header("X-3", "3")
    }
}
