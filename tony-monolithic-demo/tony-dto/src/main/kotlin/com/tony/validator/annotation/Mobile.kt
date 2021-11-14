package com.tony.validator.annotation

import com.tony.validator.MobileValidator

import javax.validation.Constraint
import javax.validation.Payload

import kotlin.reflect.KClass

/**
 * 指定范围内校验.
 *
 * @author tangli
 * @since 2018/7/11
 */
@MustBeDocumented
@Constraint(validatedBy = [MobileValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Mobile(
    val message: String = "手机号格式不正确",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
