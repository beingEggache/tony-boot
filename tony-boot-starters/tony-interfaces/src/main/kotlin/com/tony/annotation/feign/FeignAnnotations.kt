package com.tony.annotation.feign

import com.tony.feign.interceptor.request.BeanType
import com.tony.feign.interceptor.request.RequestProcessor
import kotlin.reflect.KClass

/**
 * FeignUseGlobalRequestInterceptor.
 *
 * When annotated a class, register a global request interceptor.
 *
 * Avoiding to auto register.
 *
 * @author tangli
 * @since 2023/08/02 21:00
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
 * @since 2023/08/02 21:00
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
 * @since 2023/08/02 21:00
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUnwrapResponse

/**
 * Feign global interceptors.
 *
 * with [FeignUseGlobalRequestInterceptor] and [FeignUseGlobalResponseInterceptor]
 * @author tangli
 * @since 2023/08/02 21:00
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
 * @since 2023/8/15 9:59
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class RequestProcessors(
    vararg val values: Value,
) {
    @Target()
    @Retention(AnnotationRetention.RUNTIME)
    public annotation class Value(
        val value: KClass<out RequestProcessor> = RequestProcessor::class,
        val name: String = "",
        val type: BeanType = BeanType.CLASS,
    )
}
