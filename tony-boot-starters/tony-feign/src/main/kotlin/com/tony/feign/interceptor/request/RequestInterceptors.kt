/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.feign.interceptor.request

/**
 * Feign 请求拦截器 provider.
 *
 * 用这个避免自动注册.
 *
 * @author Tang Li
 * @date 2023/08/02 19:00
 */
import com.tony.SpringContexts
import com.tony.annotation.feign.RequestProcessors
import com.tony.utils.annotation
import com.tony.utils.getLogger
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
 * @date 2023/08/02 19:00
 */
public class GlobalRequestInterceptorProvider<T : RequestInterceptor>(
    private val obj: T,
) : ObjectProvider<T> {
    override fun getObject(vararg args: Any?): T =
        obj

    override fun getObject(): T =
        obj

    override fun getIfAvailable(): T =
        obj

    override fun getIfUnique(): T =
        obj
}

/**
 * 使用请求处理器请求拦截器
 * @author Tang Li
 * @date 2023/09/13 19:33
 * @since 1.0.0
 */
public class UseRequestProcessorsRequestInterceptor : RequestInterceptor {
    private val logger = getLogger()

    override fun apply(template: RequestTemplate) {
        val configKey =
            template
                .methodMetadata()
                .configKey()

        if (feignRequestProcessorMap[configKey] != null) {
            return
        }

        val annotationOnClass =
            template
                .feignTarget()
                .type()
                .annotation(RequestProcessors::class.java)

        val annotationOnMethod =
            template
                .methodMetadata()
                .method()
                .annotation(RequestProcessors::class.java)

        if (annotationOnClass == null && annotationOnMethod == null) {
            return
        }

        val requestProcessorsValuesOnClass =
            annotationOnClass?.values.orEmpty()
        val requestProcessorsValuesOnMethod =
            annotationOnMethod?.values.orEmpty()

        val requestProcessorsValues = linkedSetOf(*requestProcessorsValuesOnClass, *requestProcessorsValuesOnMethod)

        feignRequestProcessorMap
            .getOrPut(
                configKey
            ) {
                requestProcessorsValues.map { requestProcessorValue ->
                    if (requestProcessorValue.type == BeanType.CLASS) {
                        SpringContexts.getBean(requestProcessorValue.value.java)
                    } else {
                        SpringContexts.getBean(requestProcessorValue.name, requestProcessorValue.value.java)
                    }
                }
            }.forEach { requestProcessor ->
                logger.info(
                    "Feign method[{}] apply RequestProcessor[{}]",
                    configKey,
                    requestProcessor::class.java.simpleName
                )
                requestProcessor(template)
            }
    }

    internal companion object {
        @get:JvmSynthetic
        internal val feignRequestProcessorMap: MutableMap<String, List<RequestProcessor>> =
            ConcurrentHashMap()
    }
}
