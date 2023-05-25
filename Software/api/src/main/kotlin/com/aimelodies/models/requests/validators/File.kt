package com.aimelodies.models.requests.validators

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FileValidator::class])
annotation class File(
    val message: String = "Invalid file",
    val allowedExtensions: Array<String> = [],
    val maximumBytes: Long = 1 * 1024 * 1024,
    val nullable: Boolean = true,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)