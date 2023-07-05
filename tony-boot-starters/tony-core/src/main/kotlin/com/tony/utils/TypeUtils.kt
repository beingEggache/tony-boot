@file:JvmName("TypeUtils")

package com.tony.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.TemporalAccessor
import java.util.Date

/**
 * TypeUtils is
 * @author tangli
 * @since 2023/06/07 10:53
 */

/**
 * Returns the Type object representing the class or interface that declared this type.
 *
 * @return the Type object representing the class or interface that declared this type
 */
public fun Type.rawClass(): Class<*> =
    when (this) {
        is Class<*> -> this
        is ParameterizedType -> this.rawType as Class<*>
        else -> throw IllegalStateException("Ain't gonna happen.")
    }

public fun Type.toJavaType(): JavaType = TypeFactory.defaultInstance().constructType(this)

/**
 * 返回范型参数的 [Type]
 *
 * @param index 范型参数索引, 默认为第一个,也就是 0.
 * @return Type object representing the actual type arguments to this type
 */
@JvmOverloads
public fun Class<*>.typeParamOfSuperClass(index: Int = 0): Type {
    val superClass = this.genericSuperclass
    if (superClass is Class<*>) {
        throw IllegalStateException("${superClass.simpleName} constructed without actual type information")
    }
    return (superClass as ParameterizedType).actualTypeArguments[index]
}

@JvmOverloads
public fun Class<*>.typeParamOfSuperInterface(type: Class<*>, index: Int = 0): Type {
    val genericInterfaces = this.genericInterfaces
    val matchedInterface = genericInterfaces.firstOrNull {
        it.rawClass().name == type.typeName
    } ?: throw IllegalStateException("$this does not implement the $type")
    if (matchedInterface is Class<*>) {
        throw IllegalStateException("${matchedInterface.simpleName} constructed without actual type information")
    }
    return (matchedInterface as ParameterizedType).actualTypeArguments[index]
}

/**
 * Type name clear bounds
 * @see sun.reflect.generics.reflectiveObjects.WildcardTypeImpl.toString
 */
public val Type.typeNameClearBounds: String
    get() = typeName
        .replace("? super ", "")
        .replace("? extends ", "")

internal fun Class<*>.isTypeOrSubTypeOf(type: Class<*>?): Boolean =
    (this == type) || type?.isAssignableFrom(this) == true

/**
 * 检查 是否 是某些类型 或某些类型的子类
 *
 * @param types
 * @return
 */
public fun Class<*>.isTypesOrSubTypesOf(vararg types: Class<*>?): Boolean =
    types.any { this.isTypeOrSubTypeOf(it) }

private val NUMBER_TYPES: Array<Class<*>?> =
    arrayOf(
        Long::class.javaObjectType, Long::class.javaPrimitiveType,
        Int::class.javaObjectType, Int::class.javaPrimitiveType,
        Double::class.javaObjectType, Double::class.javaPrimitiveType,
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType,
        Short::class.javaObjectType, Short::class.javaPrimitiveType,
        Float::class.javaObjectType, Float::class.javaPrimitiveType,
        BigInteger::class.java,
        BigDecimal::class.java,
    )

/**
 * 检查 是否数值类型
 */
public fun Class<*>.isNumberTypes(): Boolean = isTypesOrSubTypesOf(*NUMBER_TYPES)

/**
 * 检查 是否字符串类型
 */
public fun Class<*>.isStringLikeType(): Boolean =
    this.isTypesOrSubTypesOf(
        CharSequence::class.java,
    )

/**
 * 检查 是否列表或数组类型
 */
public fun Class<*>.isCollectionLike(): Boolean =
    this.isTypeOrSubTypeOf(Collection::class.java) ||
        this::class.java.isArray ||
        this.isArray

@Suppress("UNCHECKED_CAST")
public fun <T> TypeReference<T>.rawClass(): Class<T> =
    when (type) {
        is ParameterizedType -> (type as ParameterizedType).rawType
        else -> type
    } as Class<T>

public fun <T> TypeReference<T>.isStringLikeType(): Boolean =
    rawClass().isTypesOrSubTypesOf(
        CharSequence::class.java,
        Char::class.javaObjectType,
        Char::class.javaPrimitiveType,
    )

public fun <T> TypeReference<T>.isNumberTypes(): Boolean = rawClass().isNumberTypes()

@Suppress("UNCHECKED_CAST")
public fun <T> JavaType.rawClass(): Class<T> = rawClass as Class<T>

public fun JavaType.isDateTimeLikeType(): Boolean =
    isTypeOrSubTypeOf(Date::class.java) || isTypeOrSubTypeOf(TemporalAccessor::class.java)

public fun JavaType.isArrayLikeType(): Boolean =
    isArrayType || isCollectionLikeType

public fun JavaType.isBooleanType(): Boolean =
    isTypeOrSubTypeOf(Boolean::class.javaObjectType) || isTypeOrSubTypeOf(Boolean::class.javaPrimitiveType)

public fun JavaType.isNumberType(): Boolean =
    isTypeOrSubTypeOf(Number::class.java)

public fun JavaType.isByteType(): Boolean =
    isTypeOrSubTypeOf(Byte::class.javaObjectType) || isTypeOrSubTypeOf(Byte::class.javaPrimitiveType)

public fun JavaType.isShortType(): Boolean =
    isTypeOrSubTypeOf(Short::class.javaObjectType) || isTypeOrSubTypeOf(Short::class.javaPrimitiveType)

public fun JavaType.isIntType(): Boolean =
    isTypeOrSubTypeOf(Int::class.javaObjectType) || isTypeOrSubTypeOf(Int::class.javaPrimitiveType)

public fun JavaType.isLongType(): Boolean =
    isTypeOrSubTypeOf(Long::class.javaObjectType) || isTypeOrSubTypeOf(Long::class.javaPrimitiveType)

public fun JavaType.isFloatType(): Boolean =
    isTypeOrSubTypeOf(Float::class.javaObjectType) || isTypeOrSubTypeOf(Float::class.javaPrimitiveType)

public fun JavaType.isDoubleType(): Boolean =
    isTypeOrSubTypeOf(Double::class.javaObjectType) || isTypeOrSubTypeOf(Double::class.javaPrimitiveType)

public fun JavaType.isObjLikeType(): Boolean =
    isMapLikeType || (!isArrayLikeType() && !isNumberType() && !isStringLikeType())

public fun JavaType.isStringLikeType(): Boolean =
    isTypeOrSubTypeOf(CharSequence::class.java) ||
        isTypeOrSubTypeOf(Char::class.javaObjectType) ||
        isTypeOrSubTypeOf(Char::class.javaPrimitiveType)
