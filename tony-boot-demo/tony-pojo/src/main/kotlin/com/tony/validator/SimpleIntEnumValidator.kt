package com.tony.validator

import com.tony.core.enums.EnumCreator
import com.tony.core.enums.EnumValue
import com.tony.validator.annotation.SimpleEnum
import com.tony.validator.annotation.SimpleIntEnum
import com.tony.validator.annotation.SimpleStringEnum
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 范围校验器.
 *
 * @author tangli
 * @since 2018/7/11
 */
class SimpleIntEnumValidator : ConstraintValidator<SimpleIntEnum, EnumValue<Int>?> {
    private var range = intArrayOf()
    private var required = false
    override fun initialize(constraintAnnotation: SimpleIntEnum) {
        range = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(str: EnumValue<Int>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val value = str?.value ?: return true
        if (value == EnumCreator.defaultIntValue && required) return false
        return value in range
    }
}

class SimpleStringEnumValidator : ConstraintValidator<SimpleStringEnum, EnumValue<String>?> {

    private lateinit var enums: Array<out String>
    private var required = false
    override fun initialize(constraintAnnotation: SimpleStringEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(str: EnumValue<String>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val value = str?.value ?: return true
        if (value == EnumCreator.defaultStringValue && required) return false
        return value in enums
    }
}

class SimpleEnumValidator : ConstraintValidator<SimpleEnum, Any?> {

    private lateinit var enums: Array<out String>

    override fun initialize(constraintAnnotation: SimpleEnum) {
        enums = constraintAnnotation.enums
    }

    override fun isValid(str: Any?, constraintValidatorContext: ConstraintValidatorContext) =
        null == str
            || str.toString().isBlank()
            || str.toString() in enums
}
