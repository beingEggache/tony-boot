package com.tony.feign

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.annotation.feign.FeignUseGlobalRequestInterceptor
import com.tony.annotation.feign.FeignUseGlobalResponseInterceptor
import com.tony.feign.interceptor.response.UnwrapResponseInterceptor
import com.tony.utils.doIf
import com.tony.utils.hasAnnotation
import feign.Feign
import feign.RequestInterceptor
import feign.ResponseInterceptor
import feign.Target
import org.springframework.cloud.openfeign.FeignClientFactory
import org.springframework.cloud.openfeign.FeignClientFactoryBean
import org.springframework.cloud.openfeign.Targeter

/**
 * with [FeignUseGlobalRequestInterceptor] and [FeignUseGlobalResponseInterceptor].
 *
 * Register interceptors explicitly.
 *
 * Avoiding to auto register.
 *
 * @property globalRequestInterceptors
 * @property globalResponseInterceptors
 * @author tangli
 * @since 2023/08/02 21:00
 */
public class FeignTargeter(
    private val globalRequestInterceptors: List<RequestInterceptor>,
    private val globalResponseInterceptors: List<ResponseInterceptor>,
) : Targeter {
    override fun <T : Any?> target(
        factory: FeignClientFactoryBean,
        feign: Feign.Builder,
        context: FeignClientFactory,
        target: Target.HardCodedTarget<T>,
    ): T {
        val type = target.type()
        return feign
            .doIf(type.hasAnnotation(FeignUnwrapResponse::class.java)) {
                responseInterceptor(UnwrapResponseInterceptor())
            }
            .doIf(type.hasAnnotation(FeignUseGlobalRequestInterceptor::class.java)) {
                globalRequestInterceptors.forEach { requestInterceptor(it) }
            }
            .doIf(type.hasAnnotation(FeignUseGlobalResponseInterceptor::class.java)) {
                globalResponseInterceptors.forEach { responseInterceptor(it) }
            }
            .target(target)
    }
}
