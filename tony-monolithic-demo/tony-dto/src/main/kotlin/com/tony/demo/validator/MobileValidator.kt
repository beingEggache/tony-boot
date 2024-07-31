package com.tony.demo.validator

import com.tony.demo.validator.annotation.Mobile
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 *
 * @author tangli
 * @date 2020-11-03 16:25
 */
class MobileValidator : ConstraintValidator<Mobile, CharSequence?> {
    private val mobileRegex = Regex("^1[0-9]{10}$")

    override fun isValid(
        value: CharSequence?,
        context: ConstraintValidatorContext,
    ) = value == null || mobileRegex.matches(value)
}
