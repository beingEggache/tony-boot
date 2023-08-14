package com.tony.feign.test.module.targeter

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.GlobalRequestInterceptorProvider
import feign.Request
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import kotlin.reflect.KClass

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
        template.request()
        template.methodMetadata().method().annotations.forEach { println(it) }
    }
}


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FeignRequestProcesses(
    val values: Array<FeignRequestProcessValue>
)

@Target()
@Retention(AnnotationRetention.RUNTIME)
annotation class FeignRequestProcessValue(
    val value: FeignRequestProcessValueType = FeignRequestProcessValueType.CLASS,
    val clazz: KClass<out FeignRequestProcess> = DefaultFeignRequestProcess::class,
    val name: String = ""
)

enum class FeignRequestProcessValueType {
    CLASS,
    NAME
}

interface FeignRequestProcess {
    fun process(request: Request) {}
}

class DefaultFeignRequestProcess : FeignRequestProcess


