package com.aimelodies.models.requests

import com.aimelodies.models.domain.Genre
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class GenreRequest(
    val id: Long = 0,

    @field:NotBlank(message = "Genre name cannot be blank")
    @field:Size(max = 50, message = "The maximum genre name length is 50 characters")
    val name: String = ""
) {

    fun toGenre() = Genre(
        id = id,
        name = name
    )
}