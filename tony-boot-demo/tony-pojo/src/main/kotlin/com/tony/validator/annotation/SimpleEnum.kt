package com.tony.validator.annotation

import com.tony.validator.SimpleEnumValidator
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
@Constraint(validatedBy = [SimpleEnumValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SimpleEnum(
    vararg val enums: String,
    val message: String = "非法参数",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [])
