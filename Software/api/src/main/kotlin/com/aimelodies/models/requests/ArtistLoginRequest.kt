package com.aimelodies.models.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ArtistLoginRequest(
    @field:NotBlank(message = "Username cannot be blank")
    @field:Size(max = 50, message = "The maximum username length is 50 characters")
    val username: String = "",

    @field:NotBlank(message = "Password cannot be blank")
    val password: String = ""
)