package com.tony.web.advice

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * RequestBodyInject is
 * @author tangli
 * @since 2023/06/08 09:26
 */
@Target(
    AnnotationTarget.CLASS,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InjectRequestBody(
    val fieldNames: Array<String>
)


@JsonIgnore
@Target(
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InjectRequestBodyField
