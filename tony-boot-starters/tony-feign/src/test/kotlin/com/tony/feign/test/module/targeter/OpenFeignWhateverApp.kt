package com.tony.feign.test.module.targeter

import com.tony.annotation.EnableTonyBoot
import com.tony.traceIdHeaderName
import com.tony.utils.mdcPutOrGetDefault
import feign.Feign
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
class OpenFeignWhateverApp {
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
        traceIdHeaderName to mdcPutOrGetDefault(traceIdHeaderName)
        return feign.requestInterceptor {
            it.header("X-Whatever", "true")
            it.header("X-Go-Fuck-Yourself", "yes")
        }.target(target)
    }

}
