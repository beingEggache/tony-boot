@file:JvmName("TypeUtils")

package com.tony.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.TemporalAccessor
import java.util.Date

/**
 * TypeUtils is
 * @author tangli
 * @since 2023/06/07 10:53
 */

private val NUMBER_TYPES: Array<Class<out Number>> = arrayOf(
    Byte::class.java,
    java.lang.Byte::class.java,
    Short::class.java,
    java.lang.Short::class.java,
    Int::class.java,
    java.lang.Integer::class.java,
    Long::class.java,
    java.lang.Long::class.java,
    BigInteger::class.java,
    Float::class.java,
    java.lang.Float::class.java,
    Double::class.java,
    java.lang.Double::class.java,
    BigDecimal::class.java,
)

public fun Class<*>.isTypeOrSubTypeOf(type: Class<*>): Boolean =
    (this == type) || type.isAssignableFrom(this)

public fun Class<*>.isTypeOrSubTypesOf(vararg types: Class<*>): Boolean =
    types.any { this.isTypeOrSubTypeOf(it) }

public fun Class<*>.isNumberTypes(): Boolean = isTypeOrSubTypesOf(*NUMBER_TYPES)

public fun Class<*>.isStringLikeType(): Boolean =
    this.isTypeOrSubTypesOf(
        CharSequence::class.java,
    )

@Suppress("UNCHECKED_CAST")
public fun <T> TypeReference<T>.rawClass(): Class<T> = when (type) {
    is ParameterizedType -> (type as ParameterizedType).rawType
    else -> type
} as Class<T>

public fun <T> TypeReference<T>.isStringLikeType(): Boolean =
    rawClass().isTypeOrSubTypesOf(
        CharSequence::class.java,
        Character::class.java,
        Char::class.java,
    )

public fun <T> TypeReference<T>.isNumberTypes(): Boolean = rawClass().isNumberTypes()

@Suppress("UNCHECKED_CAST")
public fun <T> JavaType.rawClass(): Class<T> = rawClass as Class<T>

public fun JavaType.isDateTimeLikeType(): Boolean =
    isTypeOrSubTypeOf(Date::class.java) || isTypeOrSubTypeOf(TemporalAccessor::class.java)

public fun JavaType.isArrayLikeType(): Boolean =
    isArrayType || isCollectionLikeType

public fun JavaType.isBooleanType(): Boolean =
    isTypeOrSubTypeOf(Boolean::class.java) || isTypeOrSubTypeOf(java.lang.Boolean::class.java)

public fun JavaType.isNumberType(): Boolean =
    isTypeOrSubTypeOf(Number::class.java)

public fun JavaType.isByteType(): Boolean =
    isTypeOrSubTypeOf(Byte::class.java) || isTypeOrSubTypeOf(java.lang.Byte::class.java)

public fun JavaType.isShortType(): Boolean =
    isTypeOrSubTypeOf(Short::class.java) || isTypeOrSubTypeOf(java.lang.Short::class.java)

public fun JavaType.isIntType(): Boolean =
    isTypeOrSubTypeOf(Int::class.java) || isTypeOrSubTypeOf(java.lang.Integer::class.java)

public fun JavaType.isLongType(): Boolean =
    isTypeOrSubTypeOf(Long::class.java) || isTypeOrSubTypeOf(java.lang.Long::class.java)

public fun JavaType.isFloatType(): Boolean =
    isTypeOrSubTypeOf(Float::class.java) || isTypeOrSubTypeOf(java.lang.Float::class.java)

public fun JavaType.isDoubleType(): Boolean =
    isTypeOrSubTypeOf(Double::class.java) || isTypeOrSubTypeOf(java.lang.Double::class.java)

public fun JavaType.isObjLikeType(): Boolean =
    isMapLikeType || (!isArrayLikeType() && !isNumberType() && !isStringLikeType())

public fun JavaType.isStringLikeType(): Boolean =
    isTypeOrSubTypeOf(CharSequence::class.java) ||
        isTypeOrSubTypeOf(Character::class.java) ||
        isTypeOrSubTypeOf(Char::class.java)
