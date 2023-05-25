package com.aimelodies.models.requests.validators

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

class FileValidator : ConstraintValidator<File, MultipartFile?> {

    private var allowedExtensions: Array<String> = arrayOf()
    private var maximumBytes: Long = 1 * 1024 * 1024

    override fun initialize(constraintAnnotation: File) {
        allowedExtensions = constraintAnnotation.allowedExtensions
        maximumBytes = constraintAnnotation.maximumBytes
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext?): Boolean =
        if (value == null)
            true
        else
            value.isEmpty.not() &&
                (StringUtils.getFilenameExtension(value.originalFilename) in allowedExtensions) &&
                value.size <= maximumBytes
}