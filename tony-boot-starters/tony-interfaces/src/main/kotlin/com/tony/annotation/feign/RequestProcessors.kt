package com.tony.annotation.feign

import com.tony.feign.interceptor.request.BeanType
import com.tony.feign.interceptor.request.RequestProcessor
import kotlin.reflect.KClass

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
