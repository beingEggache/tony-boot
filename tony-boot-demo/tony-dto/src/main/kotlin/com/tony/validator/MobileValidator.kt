package com.tony.validator

import com.tony.validator.annotation.Mobile
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 *
 * @author tangli
 * @since 2020-11-03 16:25
 */
class MobileValidator : ConstraintValidator<Mobile, CharSequence?> {

    private val mobileRegex = Regex("^1[0-9]{10}$")

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext) =
        value == null || mobileRegex.matches(value)
}
