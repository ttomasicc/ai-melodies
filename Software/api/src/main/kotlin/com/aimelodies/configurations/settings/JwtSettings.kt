package com.aimelodies.configurations.settings

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.time.DurationMin
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.time.Duration

@Validated
@ConfigurationProperties(prefix = "app.jwt")
data class JwtSettings @ConstructorBinding constructor(
    @field:NotBlank(message = "Missing JWT secret key")
    @field:Size(min = 64, message = "JWT secret key must be at least 256 bits (64 chars)")
    val secretKey: String,
    @field:NotBlank(message = "Missing JWT Issuer")
    val issuer: String,
    @field:DurationMin(seconds = 3L, message = "JWT must be valid for at least 3 seconds")
    val duration: Duration = Duration.ofSeconds(10),
    @field:NotBlank(message = "JWT session key cannot be blank")
    val sessionKey: String = "JWT_SESSION"
)