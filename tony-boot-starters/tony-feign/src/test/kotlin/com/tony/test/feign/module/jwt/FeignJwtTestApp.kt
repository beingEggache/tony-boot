package com.tony.test.feign.module.jwt

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.request.RequestProcessor
import com.tony.utils.ifNullOrBlank
import feign.RequestTemplate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class FeignJwtTestApp {

    @Bean
    fun addTokenRequestProcessor(): AddTokenRequestProcessor =
        AddTokenRequestProcessor()

}

val mockWebAttributes = mutableMapOf<String, String>()

class AddTokenRequestProcessor : RequestProcessor {
    override fun invoke(template: RequestTemplate) {
        template.header("X-Token", mockWebAttributes["token"].ifNullOrBlank())
    }
}
