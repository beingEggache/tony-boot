package com.tony.feign.test.module.targeter

import com.tony.annotation.EnableTonyBoot
import com.tony.utils.getLogger
import feign.Feign
import feign.InvocationContext
import feign.RequestInterceptor
import feign.RequestTemplate
import feign.ResponseInterceptor
import feign.Target
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClientFactory
import org.springframework.cloud.openfeign.FeignClientFactoryBean
import org.springframework.cloud.openfeign.Targeter
import org.springframework.context.annotation.Bean

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestTargeterApp {
    @Bean
    fun customTargeter() = CustomTargeter()
}

class CustomTargeter : Targeter {
    override fun <T : Any?> target(
        factory: FeignClientFactoryBean,
        feign: Feign.Builder,
        context: FeignClientFactory,
        target: Target.HardCodedTarget<T>
    ): T {
        return feign
            .requestInterceptor(GlobalRequestInterceptor())
            .responseInterceptor(GlobalResponseInterceptor())
            .target(target)
    }
}

class GlobalRequestInterceptor : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        getLogger().info("target class:{}", template.feignTarget().type().name)
        template.header("X-Whatever", "true")
        template.header("X-Go-Fuck-Yourself", "yes")
    }
}

class GlobalResponseInterceptor : ResponseInterceptor {
    override fun aroundDecode(invocationContext: InvocationContext): Any {
        val returnType = invocationContext.returnType()
        getLogger().info("return type:{}", returnType)
        val response = invocationContext.response()
        val body = response.body()
        val inputStream = body.asInputStream()
        val bytes = inputStream.readBytes()
        val bodyString = bytes.decodeToString()
        val newResponse = response.toBuilder().body(bytes).build()
        getLogger().info("body:{}", bodyString)
        return invocationContext.decoder().decode(newResponse, returnType)
    }
}
