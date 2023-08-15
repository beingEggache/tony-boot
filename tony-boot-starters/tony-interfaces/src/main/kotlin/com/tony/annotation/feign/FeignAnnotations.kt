package com.tony.annotation.feign

import com.tony.feign.interceptor.request.BeanType
import com.tony.feign.interceptor.request.RequestProcessor
import kotlin.reflect.KClass

/**
 * Feign request add global header
 * @author tangli
 * @since 2023/08/02 21:00
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUseGlobalRequestInterceptor

/**
 * Feign response unwrap
 * @author tangli
 * @since 2023/08/02 21:00
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class FeignUseGlobalResponseInterceptor

/**
 * Feign global process.
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

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class RequestProcessors(
    val values: Array<Value>,
) {
    @Target()
    @Retention(AnnotationRetention.RUNTIME)
    public annotation class Value(
        val value: KClass<out RequestProcessor> = RequestProcessor::class,
        val name: String = "",
        val type: BeanType = BeanType.CLASS,
    )
}
