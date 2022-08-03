/**
 *
 * @author tangli
 * @since 2021-05-19 10:58
 */
package com.tony.enums.validate

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [SimpleEnumValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class SimpleEnum(
    vararg val enums: String,
    val message: String = "非法参数",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@MustBeDocumented
@Constraint(validatedBy = [SimpleIntEnumValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class SimpleIntEnum(
    vararg val enums: Int,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@MustBeDocumented
@Constraint(validatedBy = [IntEnumValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class IntEnum(
    val enumClass: KClass<*>,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@MustBeDocumented
@Constraint(validatedBy = [SimpleIntEnumValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class SimpleStringEnum(
    vararg val enums: String,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@MustBeDocumented
@Constraint(validatedBy = [StringEnumValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class StringEnum(
    val enumClass: KClass<*>,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
