package com.tony.enums.validate

/**
 *
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * 简单枚举校验
 * @param enums 枚举值
 *
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
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
public annotation class SimpleEnum(
    vararg val enums: String,
    val message: String = "非法参数",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

/**
 * 简单整形枚举校验
 * @param enums 枚举值
 * @param required 是否必须
 *
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
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
public annotation class SimpleIntEnum(
    vararg val enums: Int,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

/**
 * 整形枚举校验
 * @param enumClass 枚举类
 * @param required 是否必须
 *
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
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
public annotation class IntEnum(
    val enumClass: KClass<*>,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

/**
 * 简单字符串枚举校验
 * @param enums 枚举值
 * @param required 是否必须
 *
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
@MustBeDocumented
@Constraint(validatedBy = [SimpleStringEnumValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class SimpleStringEnum(
    vararg val enums: String,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

/**
 * 字符串枚举校验
 * @param enumClass 枚举类
 * @param required 是否必须
 *
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
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
public annotation class StringEnum(
    val enumClass: KClass<*>,
    val message: String = "非法参数",
    val required: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
