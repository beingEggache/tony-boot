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
 * 枚举校验器
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.DEFAULT_STRING_VALUE
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue
import com.tony.utils.asTo
import com.tony.utils.isTypesOrSubTypesOf
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 简单整形枚举校验
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
public class SimpleIntEnumValidator : ConstraintValidator<SimpleIntEnum, IntEnumValue?> {
    private var enums: IntArray = intArrayOf()
    private var required = false

    override fun initialize(constraintAnnotation: SimpleIntEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(
        str: IntEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required && (
                str?.value == null ||
                    str.value == DEFAULT_INT_VALUE
            )
        ) {
            return false
        }
        val value = str?.value ?: return false
        return value in enums
    }
}

/**
 * 整形枚举强类型校验
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
public class IntEnumValidator : ConstraintValidator<IntEnum, IntEnumValue?> {
    private var enums: List<Int> = listOf()
    private var required = false

    override fun initialize(constraintAnnotation: IntEnum) {
        val clazz = constraintAnnotation.enumClass.java
        if (clazz.isEnum && clazz.isTypesOrSubTypesOf(IntEnumValue::class.java)) {
            enums = clazz.enumConstants.mapNotNull { it.asTo<IntEnumValue>()?.value }
            required = constraintAnnotation.required
            return
        }
        throw IllegalStateException("class is not valid")
    }

    override fun isValid(
        str: IntEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required && (
                str?.value == null ||
                    str.value == DEFAULT_INT_VALUE
            )
        ) {
            return false
        }
        return str?.value in enums
    }
}

/**
 * 字符串枚举简单校验
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
public class SimpleStringEnumValidator : ConstraintValidator<SimpleStringEnum, StringEnumValue?> {
    private lateinit var enums: Array<out String>
    private var required = false

    override fun initialize(constraintAnnotation: SimpleStringEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(
        str: StringEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required && (
                str?.value == null ||
                    str.value == DEFAULT_STRING_VALUE
            )
        ) {
            return false
        }
        return str?.value in enums
    }
}

/**
 * 字符串枚举强类型校验
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
public class StringEnumValidator : ConstraintValidator<StringEnum, StringEnumValue?> {
    private var enums: List<String> = listOf()
    private var required = false

    override fun initialize(constraintAnnotation: StringEnum) {
        val clazz = constraintAnnotation.enumClass.java
        if (clazz.isEnum && clazz.isTypesOrSubTypesOf(StringEnumValue::class.java)) {
            enums = clazz.enumConstants.mapNotNull { it.asTo<StringEnumValue>()?.value }
            required = constraintAnnotation.required
            return
        }
        throw IllegalStateException("class is not valid")
    }

    override fun isValid(
        str: StringEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required && (
                str?.value == null ||
                    str.value == DEFAULT_STRING_VALUE
            )
        ) {
            return false
        }
        return str?.value in enums
    }
}

/**
 * 简单字符串枚举校验
 * @author Tang Li
 * @date 2021-05-19 10:58
 */
public class SimpleEnumValidator : ConstraintValidator<SimpleEnum, Any?> {
    private lateinit var enums: Array<out String>

    override fun initialize(constraintAnnotation: SimpleEnum) {
        enums = constraintAnnotation.enums
    }

    override fun isValid(
        str: Any?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean =
        null == str ||
            str.toString().isBlank() ||
            str.toString() in enums
}
