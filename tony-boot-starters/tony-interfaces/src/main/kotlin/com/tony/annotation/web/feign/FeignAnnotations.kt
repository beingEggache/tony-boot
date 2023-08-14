package com.tony.annotation.web.feign

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
