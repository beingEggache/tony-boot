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

package tony.annotation.feign
/**
 * Feign 相关注解.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
import kotlin.reflect.KClass
import tony.feign.interceptor.request.BeanType
import tony.feign.interceptor.request.RequestProcessor
import tony.feign.interceptor.response.UnwrapResponseInterceptor

/**
 * FeignUseGlobalRequestInterceptor.
 *
 * When annotated a class, register a global request interceptor.
 *
 * Avoiding to auto register.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUseGlobalRequestInterceptor

/**
 * FeignUseGlobalResponseInterceptor.
 *
 * When annotated a class, register a global response interceptor.
 *
 * Avoiding to auto register.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUseGlobalResponseInterceptor

/**
 * FeignUnwrapResponse.
 *
 * When annotated a class, register a global response interceptor.
 *
 * Avoiding to auto register.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUnwrapResponse(
    val type: KClass<out UnwrapResponseInterceptor> = UnwrapResponseInterceptor::class,
)

/**
 * Feign global interceptors.
 *
 * with [FeignUseGlobalRequestInterceptor] and [FeignUseGlobalResponseInterceptor]
 * @author tangli
 * @date 2023/08/02 19:00
 */
@FeignUseGlobalResponseInterceptor
@FeignUseGlobalRequestInterceptor
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUseGlobalInterceptor

/**
 * With [FeignUseGlobalRequestInterceptor] to apply processors explicitly.
 *
 * Avoiding to auto register.
 *
 * @author tangli
 * @date 2023/8/15 9:59
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class RequestProcessors(
    vararg val values: Value,
) {
    @Target
    @Retention(AnnotationRetention.RUNTIME)
    public annotation class Value(
        val value: KClass<out RequestProcessor> = RequestProcessor::class,
        val name: String = "",
        val type: BeanType = BeanType.CLASS,
    )
}
