package com.aimelodies.controllers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.web.server.ResponseStatusException

@Component
class ControllerValidator {

    fun verifyNonNullErrors(bindingResult: BindingResult, exceptions: Iterable<String> = listOf()) {
        if (bindingResult.hasErrors()) {
            val fieldErrorList = bindingResult.fieldErrors.filter {
                it.rejectedValue != null || it.field in exceptions
            }
            if (fieldErrorList.isNotEmpty())
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldErrorList.joinToString(separator = "; ") {
                        it.defaultMessage ?: "Unknown error"
                    }
                )
        }
    }

    fun verifyErrors(bindingResult: BindingResult) {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                bindingResult.fieldErrors.joinToString(separator = "; ") {
                    it.defaultMessage ?: "Unknown error"
                }
            )
        }
    }
}