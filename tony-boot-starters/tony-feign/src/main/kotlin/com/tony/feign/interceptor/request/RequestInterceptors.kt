package com.tony.feign.interceptor.request

import com.tony.SpringContexts
import com.tony.annotation.feign.RequestProcessors
import com.tony.utils.annotation
import feign.RequestInterceptor
import feign.RequestTemplate
import java.util.concurrent.ConcurrentHashMap
import org.springframework.beans.factory.ObjectProvider

/**
 * Global request interceptor provider.
 *
 * 用这个避免自动注册.
 *
 * @param T
 * @property obj
 * @author Tang Li
 * @date 2023/08/02 21:00
 */
public class GlobalRequestInterceptorProvider<T : RequestInterceptor>(
    private val obj: T,
) : ObjectProvider<T> {
    override fun getObject(vararg args: Any?): T = obj
    override fun getObject(): T = obj
    override fun getIfAvailable(): T = obj
    override fun getIfUnique(): T = obj
}

public class UseRequestProcessorsRequestInterceptor :
    RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val annotation = template.methodMetadata()
            .method()
            .annotation(RequestProcessors::class.java) ?: return
        val processorList = feignRequestProcessorMap.getOrPut(template.feignTarget().type()) {
            annotation.values.map {
                if (it.type == BeanType.CLASS) {
                    SpringContexts.getBean(it.value.java)
                } else {
                    SpringContexts.getBean(it.name, it.value.java)
                }
            }
        }

        processorList.forEach {
            it(template)
        }
    }

    internal companion object {
        internal val feignRequestProcessorMap: MutableMap<Class<*>, List<RequestProcessor>> = ConcurrentHashMap()
    }
}
