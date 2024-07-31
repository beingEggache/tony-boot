package com.tony.demo.annotation

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.OptBoolean

/**
 * TentantIdInject is
 * @author tangli
 * @date 2024/07/03 14:10
 * @since 1.0.0
 */

@Target(
    allowedTargets = [
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.FIELD
    ]
)
@Retention(AnnotationRetention.RUNTIME)
@JacksonAnnotationsInside
@JacksonInject("tenantId", useInput = OptBoolean.TRUE)
annotation class TenantIdInject

@Target(
    allowedTargets = [
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.FIELD
    ]
)
@Retention(AnnotationRetention.RUNTIME)
@JacksonAnnotationsInside
@JacksonInject("userId")
annotation class UserIdInject

@Target(
    allowedTargets = [
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.FIELD
    ]
)
@Retention(AnnotationRetention.RUNTIME)
@JacksonAnnotationsInside
@JacksonInject("appId", useInput = OptBoolean.TRUE)
annotation class AppIdInject
