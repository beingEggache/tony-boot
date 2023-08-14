package com.tony.feign.test.module.targeter_signature

import com.tony.SpringContexts
import com.tony.annotation.EnableTonyBoot
import com.tony.feign.interceptor.GlobalRequestInterceptorProvider
import com.tony.utils.annotation
import feign.Request
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestTargeterSignatureApp {

    @Bean("feignRequestProcess1")
    fun feignRequestProcess1(): FeignRequestProcess {
        return object : FeignRequestProcess {
            override fun process(request: Request) {
                println(1)
                request.header("X-1", "1")
            }
        }
    }

    @Bean("feignRequestProcess2")
    fun feignRequestProcess2(): FeignRequestProcess {
        return object : FeignRequestProcess {
            override fun process(request: Request) {
                println(2)
                request.header("X-2", "2")
            }
        }
    }
    @Bean
    fun feignRequestProcess3(): FeignRequestProcess3 {
        return FeignRequestProcess3()
    }

    @Bean
    fun defaultGlobalRequestInterceptor(): GlobalRequestInterceptorProvider<RequestInterceptor> =
        GlobalRequestInterceptorProvider(DefaultGlobalRequestInterceptor())

}

internal class DefaultGlobalRequestInterceptor() :
    RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val annotation = template.methodMetadata()
            .method()
            .annotation(FeignRequestProcesses::class.java) ?: return
        val processorList = feignRequestProcessorMap.getOrPut(template.feignTarget().type()) {
            annotation.values.map {
                if (it.type == FeignRequestProcessValueType.CLASS) {
                    SpringContexts.getBean(it.clazz.java)
                } else {
                    SpringContexts.getBean(it.name, it.clazz.java)
                }
            }
        }
        processorList.forEach {
            it.process(template.request())
        }

    }

    companion object {
        internal val feignRequestProcessorMap: MutableMap<Class<*>, List<FeignRequestProcess>> = ConcurrentHashMap()
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
    val type: FeignRequestProcessValueType = FeignRequestProcessValueType.CLASS,
    val clazz: KClass<out FeignRequestProcess> = FeignRequestProcess::class,
    val name: String = ""
)

enum class FeignRequestProcessValueType {
    CLASS,
    NAME
}

interface FeignRequestProcess {
    fun process(request: Request) {}
}

class FeignRequestProcess3 : FeignRequestProcess{
    override fun process(request: Request) {
        println(3)
        request.header("X-3", "3")
    }
}
