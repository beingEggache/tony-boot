package com.tony.enums.validate

/**
 *
 * @author tangli
 * @since 2021-05-19 10:58
 */
import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.DEFAULT_STRING_VALUE
import com.tony.enums.EnumIntValue
import com.tony.enums.EnumStringValue
import com.tony.enums.EnumValue
import com.tony.utils.asTo
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 简单整形枚举校验
 * @author tangli
 * @since 2021-05-19 10:58
 */
public class SimpleIntEnumValidator : ConstraintValidator<SimpleIntEnum, EnumValue<Int>?> {
    private var enums: IntArray = intArrayOf()
    private var required = false
    override fun initialize(constraintAnnotation: SimpleIntEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(str: EnumValue<Int>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
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
 * @author tangli
 * @since 2021-05-19 10:58
 */
public class IntEnumValidator : ConstraintValidator<IntEnum, EnumValue<Int>?> {
    private var enums: List<Int> = listOf()
    private var required = false
    override fun initialize(constraintAnnotation: IntEnum) {
        val clazz = constraintAnnotation.enumClass.java
        if (clazz.isEnum &&
            EnumIntValue::class.java.isAssignableFrom(clazz)
        ) {
            enums = clazz.enumConstants.mapNotNull { it.asTo<EnumIntValue>()?.value }
            required = constraintAnnotation.required
            return
        }
        throw IllegalStateException("class is not valid")
    }

    override fun isValid(str: EnumValue<Int>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
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
 * @author tangli
 * @since 2021-05-19 10:58
 */
public class SimpleStringEnumValidator : ConstraintValidator<SimpleStringEnum, EnumValue<String>?> {

    private lateinit var enums: Array<out String>
    private var required = false
    override fun initialize(constraintAnnotation: SimpleStringEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(str: EnumValue<String>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
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
 * @author tangli
 * @since 2021-05-19 10:58
 */
public class StringEnumValidator : ConstraintValidator<StringEnum, EnumValue<String>?> {
    private var enums: List<String> = listOf()
    private var required = false
    override fun initialize(constraintAnnotation: StringEnum) {
        val clazz = constraintAnnotation.enumClass.java
        if (clazz.isEnum &&
            EnumStringValue::class.java.isAssignableFrom(clazz)
        ) {
            enums = clazz.enumConstants.mapNotNull { it.asTo<EnumStringValue>()?.value }
            required = constraintAnnotation.required
            return
        }
        throw IllegalStateException("class is not valid")
    }

    override fun isValid(str: EnumValue<String>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
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
 * @author tangli
 * @since 2021-05-19 10:58
 */
public class SimpleEnumValidator : ConstraintValidator<SimpleEnum, Any?> {

    private lateinit var enums: Array<out String>

    override fun initialize(constraintAnnotation: SimpleEnum) {
        enums = constraintAnnotation.enums
    }

    override fun isValid(str: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean =
        null == str ||
            str.toString().isBlank() ||
            str.toString() in enums
}
