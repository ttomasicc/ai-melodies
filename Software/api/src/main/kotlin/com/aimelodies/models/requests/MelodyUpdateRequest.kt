package com.aimelodies.models.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MelodyUpdateRequest(
    val artistId: Long = 0,
    val albumId: Long = 0,
    val melodyId: Long = 0,

    @field:NotBlank(message = "Melody name cannot be blank")
    @field:Size(max = 100, message = "Maximum melody name length is 100 characters")
    val name: String = ""
)