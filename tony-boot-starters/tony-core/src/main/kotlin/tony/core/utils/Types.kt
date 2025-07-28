/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("Types")

package tony.core.utils

/**
 * 泛型工具类
 * @author tangli
 * @date 2023/06/07 19:53
 */
import com.fasterxml.jackson.core.type.ResolvedType
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
 * Returns the Type object representing the class or interface that declared this type.
 * @return [Class]
 * @author tangli
 * @date 2023/09/13 19:27
 */
public fun Type.rawClass(): Class<*> =
    when (this) {
        is Class<*> -> this
        is ParameterizedType -> this.rawType.asToNotNull()
        is ResolvedType -> rawClass
        else -> error("${this::class.java} doesn't support")
    }

/**
 * JavaType
 * @return [JavaType]
 * @author tangli
 * @date 2023/09/13 19:27
 */
public fun Type.toJavaType(): JavaType =
    TypeFactory.defaultInstance().constructType(this)

/**
 * Method for constructing a CollectionLikeType.
 * @param [collectionType]
 * @return [JavaType]
 * @author tangli
 * @date 2023/09/13 19:27
 */
public fun <T : Collection<*>> Type.toCollectionJavaType(collectionType: Class<T>): JavaType =
    TypeFactory
        .defaultInstance()
        .constructParametricType(collectionType, this.toJavaType())

/**
 * 返回父类范型参数的  [Type]
 * @param [index] 范型参数索引, 默认为第一个,也就是 0.
 * @return [Type]
 * @author tangli
 * @date 2023/09/13 19:28
 */
@JvmOverloads
public fun Class<*>.typeParamOfSuperClass(index: Int = 0): Type {
    val superClass = this.genericSuperclass
    check(superClass !is Class<*>) { "${superClass.typeName} constructed without actual type information" }
    return superClass.asToNotNull<ParameterizedType>().actualTypeArguments[index]
}

@JvmSynthetic
internal fun Class<*>.isTypeOrSubTypeOf(type: Class<*>?): Boolean =
    (this == type) || type?.isAssignableFrom(this) == true

@JvmSynthetic
internal fun Class<*>.isTypesOrSubTypesOf(vararg types: Class<*>?): Boolean =
    types.any { this.isTypeOrSubTypeOf(it) }

@JvmSynthetic
internal fun Class<*>.isTypesOrSubTypesOf(typeCollection: Collection<Class<*>?>): Boolean =
    typeCollection.any { this.isTypeOrSubTypeOf(it) }

/**
 * 检查 是否 是某些类型 或某些类型的子类
 * @param [types] 类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:29
 */
public fun Type.isTypesOrSubTypesOf(vararg types: Class<*>?): Boolean =
    when (this) {
        is Class<*> -> types.any { this.isTypeOrSubTypeOf(it) }
        is ParameterizedType -> types.any { this.rawClass().isTypeOrSubTypeOf(it) }
        else -> false
    }

/**
 * 检查 是否 是某些类型 或某些类型的子类
 * @param [typeCollection] 类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:29
 */
public fun Type.isTypesOrSubTypesOf(typeCollection: Collection<Class<*>?>): Boolean =
    when (this) {
        is Class<*> -> typeCollection.any { this.isTypeOrSubTypeOf(it) }
        is ParameterizedType -> typeCollection.any { this.rawClass().isTypeOrSubTypeOf(it) }
        else -> false
    }

private val numberTypeCollection: List<Class<*>?> =
    listOf(
        Long::class.javaObjectType,
        Long::class.javaPrimitiveType,
        Int::class.javaObjectType,
        Int::class.javaPrimitiveType,
        Double::class.javaObjectType,
        Double::class.javaPrimitiveType,
        Byte::class.javaObjectType,
        Byte::class.javaPrimitiveType,
        Short::class.javaObjectType,
        Short::class.javaPrimitiveType,
        Float::class.javaObjectType,
        Float::class.javaPrimitiveType,
        BigInteger::class.java,
        BigDecimal::class.java
    )

/**
 * 是否[Boolean]类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun Type.isBooleanType(): Boolean =
    this == Boolean::class.java ||
        this == Boolean::class.javaPrimitiveType

/**
 * 是否数字类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:29
 */
public fun Type.isNumberTypes(): Boolean =
    isTypesOrSubTypesOf(numberTypeCollection)

/**
 * 是否数组类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun Type.isArrayType(): Boolean =
    this::class.java.isArray ||
        rawClass().isArray

/**
 * 是否列表或数组类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun Type.isArrayLikeType(): Boolean =
    this.isTypesOrSubTypesOf(Collection::class.java) ||
        this.isArrayType()

/**
 * 是否字符串类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:29
 */
public fun Type.isStringLikeType(): Boolean =
    this.isTypesOrSubTypesOf(
        CharSequence::class.java
    )

/**
 * 是否时间类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun Type.isDateTimeLikeType(): Boolean =
    this.isTypesOrSubTypesOf(Date::class.java) ||
        this.isTypesOrSubTypesOf(TemporalAccessor::class.java)

/**
 * 是否简单类型([Number], [String], [Date], [Boolean], [Enum])
 * @return [Boolean]
 * @author tangli
 * @date 2025/07/23 09:35
 */
public fun Type.isSimpleType(): Boolean =
    this.isStringLikeType() ||
        this.isNumberTypes() ||
        this.isBooleanType() ||
        this.isDateTimeLikeType() ||
        rawClass().isEnum

/**
 * 是否[Void]类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun Type.isVoidLikeType(): Boolean =
    this == Void.TYPE ||
        this == Unit::class.java

/**
 * 原始类
 * @return [Class]<[T]>
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun <T> TypeReference<T>.rawClass(): Class<T> =
    when (type) {
        is ParameterizedType -> type.asToNotNull<ParameterizedType>().rawType
        else -> type
    }.asToNotNull()

/**
 * 是类似字符串类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun <T> TypeReference<T>.isStringLikeType(): Boolean =
    rawClass().isTypesOrSubTypesOf(
        CharSequence::class.java,
        Char::class.javaObjectType,
        Char::class.javaPrimitiveType
    )

/**
 * 是数字类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun <T> TypeReference<T>.isNumberTypes(): Boolean =
    rawClass().isNumberTypes()

/**
 * rawClass
 * @return [Class]<[T]>
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun <T> JavaType.rawClass(): Class<T> =
    rawClass.asToNotNull()

/**
 * Method for constructing a CollectionLikeType.
 * @param [collectionType]
 * @return [JavaType]
 * @author tangli
 * @date 2023/09/13 19:27
 */
public fun <T : Collection<*>> JavaType.toCollectionJavaType(collectionType: Class<T>): JavaType =
    TypeFactory
        .defaultInstance()
        .constructCollectionLikeType(collectionType, this)

/**
 * 是数字类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isNumberType(): Boolean =
    isIntType() ||
        isLongType() ||
        isDoubleType() ||
        isFloatType() ||
        isByteType() ||
        isShortType() ||
        isTypeOrSubTypeOf(Number::class.java)

/**
 * 是布尔类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isBooleanType(): Boolean =
    isTypeOrSubTypeOf(Boolean::class.javaObjectType) || isTypeOrSubTypeOf(Boolean::class.javaPrimitiveType)

/**
 * 是字节类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isByteType(): Boolean =
    isTypeOrSubTypeOf(Byte::class.javaObjectType) || isTypeOrSubTypeOf(Byte::class.javaPrimitiveType)

/**
 * 是短型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isShortType(): Boolean =
    isTypeOrSubTypeOf(Short::class.javaObjectType) || isTypeOrSubTypeOf(Short::class.javaPrimitiveType)

/**
 * 是int类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isIntType(): Boolean =
    isTypeOrSubTypeOf(Int::class.javaObjectType) || isTypeOrSubTypeOf(Int::class.javaPrimitiveType)

/**
 * 是长型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isLongType(): Boolean =
    isTypeOrSubTypeOf(Long::class.javaObjectType) || isTypeOrSubTypeOf(Long::class.javaPrimitiveType)

/**
 * 是浮点型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isFloatType(): Boolean =
    isTypeOrSubTypeOf(Float::class.javaObjectType) || isTypeOrSubTypeOf(Float::class.javaPrimitiveType)

/**
 * 是双浮点类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isDoubleType(): Boolean =
    isTypeOrSubTypeOf(Double::class.javaObjectType) || isTypeOrSubTypeOf(Double::class.javaPrimitiveType)

/**
 * 是类似日期时间类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isDateTimeLikeType(): Boolean =
    isTypeOrSubTypeOf(Date::class.java) || isTypeOrSubTypeOf(TemporalAccessor::class.java)

/**
 * 是类似字符串类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:31
 */
public fun JavaType.isStringLikeType(): Boolean =
    isTypeOrSubTypeOf(CharSequence::class.java) ||
        isTypeOrSubTypeOf(Char::class.javaObjectType) ||
        isTypeOrSubTypeOf(Char::class.javaPrimitiveType)

/**
 * 是类似数组类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:30
 */
public fun JavaType.isArrayLikeType(): Boolean =
    isArrayType || isCollectionLikeType

/**
 * 是类似obj类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:31
 */
public fun JavaType.isObjLikeType(): Boolean =
    isMapLikeType ||
        (!isDateTimeLikeType() && !isBooleanType() && !isEnumType) &&
        (!isNumberType() && !isStringLikeType() && !isArrayLikeType())
