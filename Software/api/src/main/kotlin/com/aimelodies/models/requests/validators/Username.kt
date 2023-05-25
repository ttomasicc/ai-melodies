package com.aimelodies.models.requests.validators

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UsernameValidator::class])
annotation class Username(
    val message: String = "Username must contain only alphanumericals",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)