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

package tony.feign

import feign.Feign
import feign.RequestInterceptor
import feign.ResponseInterceptor
import feign.Target
import org.springframework.cloud.openfeign.FeignClientFactory
import org.springframework.cloud.openfeign.FeignClientFactoryBean
import org.springframework.cloud.openfeign.Targeter
import tony.annotation.feign.FeignUnwrapResponse
import tony.annotation.feign.FeignUseGlobalRequestInterceptor
import tony.annotation.feign.FeignUseGlobalResponseInterceptor
import tony.feign.interceptor.response.DefaultUnwrapResponseInterceptor
import tony.feign.interceptor.response.UnwrapResponseInterceptor
import tony.utils.annotation
import tony.utils.applyIf
import tony.utils.getLogger
import tony.utils.hasAnnotation
import tony.utils.throwIfNull

/**
 * FeignTargeter
 *
 * 用于自定义 Feign 客户端的拦截器注册逻辑。
 * 支持通过注解 [FeignUseGlobalRequestInterceptor]、[FeignUseGlobalResponseInterceptor]、[FeignUnwrapResponse]
 * 控制是否注册全局 request/response 拦截器和解包拦截器。
 *
 * 适用场景：
 * - 需要为部分 FeignClient 动态注册全局拦截器或解包拦截器的场景。
 * - 需要支持多种拦截器扩展和灵活组合。
 *
 * 注意事项：
 * - target 方法内部有详细日志，便于排查拦截器注册情况。
 * - 拦截器注册顺序与注解顺序一致。
 * - 如需自定义拦截器类型，请实现对应接口并在构造参数传入。
 * - 若拦截器选择或注册失败会抛出异常，建议业务方关注日志。
 *
 * @property globalRequestInterceptors 全局请求拦截器列表
 * @property globalResponseInterceptors 全局响应拦截器列表
 * @property unwrapResponseInterceptors 解包拦截器列表
 * @author tangli
 * @date 2023/08/02 19:00
 */
internal class FeignTargeter(
    private val globalRequestInterceptors: List<RequestInterceptor>,
    private val globalResponseInterceptors: List<ResponseInterceptor>,
    private val unwrapResponseInterceptors: List<UnwrapResponseInterceptor>,
) : Targeter {
    private val logger = getLogger()

    /**
     * 为 FeignClient 动态注册拦截器。
     *
     * @param factory FeignClientFactoryBean
     * @param feign Feign.Builder
     * @param context FeignClientFactory
     * @param target 目标 FeignClient
     * @return 代理对象
     *
     * 注册逻辑：
     * - 若 FeignClient 类上有 [FeignUnwrapResponse] 注解，则注册对应解包拦截器。
     * - 若有 [FeignUseGlobalRequestInterceptor] 注解，则注册全局请求拦截器。
     * - 若有 [FeignUseGlobalResponseInterceptor] 注解，则注册全局响应拦截器。
     * - 注册过程有详细日志，便于排查。
     * - 若拦截器类型未找到或注册失败会抛出异常。
     */
    override fun <T : Any?> target(
        factory: FeignClientFactoryBean,
        feign: Feign.Builder,
        context: FeignClientFactory,
        target: Target.HardCodedTarget<T>,
    ): T {
        val type = target.type()
        return feign
            .applyIf(type.hasAnnotation(FeignUnwrapResponse::class.java)) {
                val annotation = type.annotation(FeignUnwrapResponse::class.java).throwIfNull()
                val unwrapResponseInterceptorType = annotation.type
                logger.info(
                    "FeignClient[{}] apply UnwrapResponseInterceptor[{}].",
                    type.simpleName,
                    unwrapResponseInterceptorType.simpleName
                )
                if (unwrapResponseInterceptorType == UnwrapResponseInterceptor::class) {
                    responseInterceptor(
                        unwrapResponseInterceptors.first { it::class == DefaultUnwrapResponseInterceptor::class }
                    )
                } else {
                    responseInterceptor(unwrapResponseInterceptors.first { it::class == unwrapResponseInterceptorType })
                }
            }.applyIf(type.hasAnnotation(FeignUseGlobalRequestInterceptor::class.java)) {
                globalRequestInterceptors.forEach { reqInterceptor ->
                    logger.info(
                        "FeignClient[{}] apply requestInterceptor[{}].",
                        type.simpleName,
                        reqInterceptor::class.java.simpleName
                    )
                    requestInterceptor(reqInterceptor)
                }
            }.applyIf(type.hasAnnotation(FeignUseGlobalResponseInterceptor::class.java)) {
                globalResponseInterceptors.forEach { respInterceptor ->
                    logger.info(
                        "FeignClient[{}] apply responseInterceptor[{}].",
                        type.simpleName,
                        respInterceptor::class.java.simpleName
                    )
                    responseInterceptor(respInterceptor)
                }
            }.target(target)
    }
}
