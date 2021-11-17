/**
 *
 * @author tangli
 * @since 2021-05-19 10:58
 */
@file:Suppress("unused")

package com.tony.enums.validate

import com.tony.enums.EnumCreator
import com.tony.enums.EnumValue
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class SimpleIntEnumValidator : ConstraintValidator<SimpleIntEnum, EnumValue<Int>?> {
    private var enums = intArrayOf()
    private var required = false
    override fun initialize(constraintAnnotation: SimpleIntEnum) {
        enums = constraintAnnotation.enums
        required = constraintAnnotation.required
    }

    override fun isValid(str: EnumValue<Int>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val value = str?.value ?: return true
        if (value == EnumCreator.defaultIntValue && required) return false
        return value in enums
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
        null == str ||
            str.toString().isBlank() ||
            str.toString() in enums
}
