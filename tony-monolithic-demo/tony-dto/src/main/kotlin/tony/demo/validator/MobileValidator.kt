package tony.demo.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import tony.demo.validator.annotation.Mobile

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
    ) =
        value == null || mobileRegex.matches(value)
}
