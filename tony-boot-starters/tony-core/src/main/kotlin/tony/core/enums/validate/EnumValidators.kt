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

package tony.core.enums.validate

/**
 * 枚举校验器
 * @author tangli
 * @date 2021-05-19 10:58
 */
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import tony.core.enums.DEFAULT_INT_VALUE
import tony.core.enums.DEFAULT_STRING_VALUE
import tony.core.enums.IntEnumValue
import tony.core.enums.StringEnumValue
import tony.core.utils.asTo
import tony.core.utils.isTypesOrSubTypesOf

/**
 * 简单整形枚举校验
 * @author tangli
 * @date 2021-05-19 10:58
 */
public class RangedIntEnumValidator : ConstraintValidator<RangedIntEnum, IntEnumValue?> {
    private var enums: IntArray = intArrayOf()
    private var required = false

    override fun initialize(constraintAnnotation: RangedIntEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(
        enumValue: IntEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required &&
            (
                enumValue?.value == null ||
                    enumValue.value == DEFAULT_INT_VALUE
            )
        ) {
            return false
        }
        val value = enumValue?.value ?: return false
        return value in enums
    }
}

/**
 * 整形枚举强类型校验
 * @author tangli
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
        enumValue: IntEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required &&
            (
                enumValue?.value == null ||
                    enumValue.value == DEFAULT_INT_VALUE
            )
        ) {
            return false
        }
        return enumValue?.value in enums
    }
}

/**
 * 字符串枚举简单校验
 * @author tangli
 * @date 2021-05-19 10:58
 */
public class RangedStringEnumValidator : ConstraintValidator<RangedStringEnum, StringEnumValue?> {
    private lateinit var enums: Array<out String>
    private var required = false

    override fun initialize(constraintAnnotation: RangedStringEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(
        enumValue: StringEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required &&
            (
                enumValue?.value == null ||
                    enumValue.value == DEFAULT_STRING_VALUE
            )
        ) {
            return false
        }
        return enumValue?.value in enums
    }
}

/**
 * 字符串枚举强类型校验
 * @author tangli
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
        enumValue: StringEnumValue?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean {
        if (required &&
            (
                enumValue?.value == null ||
                    enumValue.value == DEFAULT_STRING_VALUE
            )
        ) {
            return false
        }
        return enumValue?.value in enums
    }
}

/**
 * 简单字符串枚举校验
 * @author tangli
 * @date 2021-05-19 10:58
 */
public class SimpleStringEnumValidator : ConstraintValidator<SimpleStringEnum, String?> {
    private lateinit var enums: Array<out String>

    override fun initialize(constraintAnnotation: SimpleStringEnum) {
        enums = constraintAnnotation.enums
    }

    override fun isValid(
        enumValue: String?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean =
        enumValue.isNullOrBlank() ||
            enumValue in enums
}

/**
 * 简单字符串枚举校验
 * @author tangli
 * @date 2021-05-19 10:58
 */
public class SimpleIntEnumValidator : ConstraintValidator<SimpleIntEnum, Int?> {
    private lateinit var enums: IntArray

    override fun initialize(constraintAnnotation: SimpleIntEnum) {
        enums = constraintAnnotation.enums
    }

    override fun isValid(
        enumValue: Int?,
        constraintValidatorContext: ConstraintValidatorContext,
    ): Boolean =
        enumValue == null ||
            enumValue in enums
}
