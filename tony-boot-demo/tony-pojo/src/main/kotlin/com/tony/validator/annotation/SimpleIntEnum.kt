package com.tony.validator.annotation

import com.tony.validator.SimpleIntEnumValidator
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
@Constraint(validatedBy = [SimpleIntEnumValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SimpleIntEnum(
    vararg val range: Int,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [])
