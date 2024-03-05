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

package com.tony.enums.validate

/**
 * 枚举转换注解
 * @author tangli
 * @date 2021-05-19 10:58
 */
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * 简单枚举校验
 * @param enums 枚举值
 *
 * @author tangli
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
 * @author tangli
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
 * @author tangli
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
 * @author tangli
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
 * @author tangli
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
