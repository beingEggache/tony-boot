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

package com.tony.feign

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.annotation.feign.FeignUseGlobalRequestInterceptor
import com.tony.annotation.feign.FeignUseGlobalResponseInterceptor
import com.tony.feign.interceptor.response.UnwrapResponseInterceptor
import com.tony.utils.applyIf
import com.tony.utils.hasAnnotation
import feign.Feign
import feign.RequestInterceptor
import feign.ResponseInterceptor
import feign.Target
import org.springframework.cloud.openfeign.FeignClientFactory
import org.springframework.cloud.openfeign.FeignClientFactoryBean
import org.springframework.cloud.openfeign.Targeter

/**
 * Feign Targeter.
 *
 * with [FeignUseGlobalRequestInterceptor] and [FeignUseGlobalResponseInterceptor].
 *
 * Register interceptors explicitly.
 *
 * Avoiding to auto register.
 *
 * @property globalRequestInterceptors
 * @property globalResponseInterceptors
 * @author Tang Li
 * @date 2023/08/02 21:00
 */
public class FeignTargeter(
    private val globalRequestInterceptors: List<RequestInterceptor>,
    private val globalResponseInterceptors: List<ResponseInterceptor>,
    private val unwrapResponseInterceptor: UnwrapResponseInterceptor,
) : Targeter {
    override fun <T : Any?> target(
        factory: FeignClientFactoryBean,
        feign: Feign.Builder,
        context: FeignClientFactory,
        target: Target.HardCodedTarget<T>,
    ): T {
        val type = target.type()
        return feign
            .applyIf(type.hasAnnotation(FeignUnwrapResponse::class.java)) {
                responseInterceptor(unwrapResponseInterceptor)
            }.applyIf(type.hasAnnotation(FeignUseGlobalRequestInterceptor::class.java)) {
                globalRequestInterceptors.forEach { requestInterceptor(it) }
            }.applyIf(type.hasAnnotation(FeignUseGlobalResponseInterceptor::class.java)) {
                globalResponseInterceptors.forEach { responseInterceptor(it) }
            }.target(target)
    }
}
