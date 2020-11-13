package com.tony.validator

import com.tony.core.enums.EnumCreator
import com.tony.core.enums.EnumValue
import com.tony.validator.annotation.SimpleIntEnum
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
        range = constraintAnnotation.range
        required = constraintAnnotation.required
    }

    override fun isValid(str: EnumValue<Int>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val value = str?.value ?: return true
        if (value == EnumCreator.defaultIntEnumValue && required) return false
        return value in range
    }
}
