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

package tony.feign.interceptor.request

/**
 * Feign 请求拦截器 provider.
 *
 * 用这个避免自动注册.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
import feign.RequestInterceptor
import feign.RequestTemplate
import java.util.concurrent.ConcurrentHashMap
import org.springframework.beans.factory.ObjectProvider
import tony.SpringContexts
import tony.annotation.feign.RequestProcessors
import tony.utils.annotation
import tony.utils.getLogger

/**
 * 全局请求拦截器 Provider。
 *
 * 用于手动注册全局 RequestInterceptor，避免自动注册带来的副作用。
 * 适用场景：需要灵活控制拦截器注册时机和范围。
 * @param obj 具体的 RequestInterceptor 实例
 * @author tangli
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
 * 动态请求处理器拦截器。
 *
 * 主要功能：
 * 1. 根据 @RequestProcessors 注解动态注册并执行 RequestProcessor。
 * 2. 支持类级和方法级注解，支持多处理器组合。
 * 3. 通过缓存提升性能，避免重复反射和 Bean 查找。
 *
 * 适用场景：
 * - 需要对部分 FeignClient 方法灵活扩展请求处理逻辑的场景。
 *
 * 类型支持：
 * - 支持任意实现 RequestProcessor 的 Bean。
 *
 * 异常行为：
 * - Bean 获取失败、处理器执行异常会抛出异常并记录日志。
 *
 * 性能影响：
 * - 处理器列表有缓存，首次注册略慢，后续高效。
 *
 * 扩展方式：
 * - 实现 RequestProcessor 并通过 @RequestProcessors 注解声明。
 *
 * 注意事项：
 * - RequestProcessor 若有状态需保证线程安全。
 * @author tangli
 * @date 2023/09/13 19:33
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
