package com.aimelodies.models.requests.validators

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UsernameValidator : ConstraintValidator<Username, String?> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean =
        if (value == null)
            true
        else
            "^[a-z0-9]+$".toRegex(RegexOption.IGNORE_CASE).matches(value.trim())
}
