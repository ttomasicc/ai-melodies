package com.aimelodies.models.requests

import com.aimelodies.models.domain.Artist
import com.aimelodies.models.enumerations.Role
import com.aimelodies.models.requests.validators.Username
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ArtistRegistrationRequest(
    @field:NotBlank(message = "Username cannot be blank")
    @field:Size(max = 50, message = "The maximum username length is 50 characters")
    @field:Username
    val username: String = "",

    @field:NotBlank(message = "Email cannot be blank")
    @field:Size(max = 70, message = "The maximum email length is 70 characters")
    @field:Email(message = "Invalid email address")
    val email: String = "",

    @field:NotBlank(message = "Password cannot be blank")
    val password: String = "",

    @field:NotBlank(message = "First name cannot be blank")
    @field:Size(max = 70, message = "The maximum first name length is 70 characters")
    val firstName: String? = null,

    @field:NotBlank(message = "Last name cannot be blank")
    @field:Size(max = 70, message = "The maximum last name length is 70 characters")
    val lastName: String? = null,

    @field:NotBlank(message = "Biography cannot be blank")
    val bio: String? = null
) {

    fun toArtist(
        roleFetcher: () -> Role,
        passwordFetcher: (password: String) -> String
    ) = Artist(
        uname = username.lowercase(),
        email = email.lowercase(),
        passwd = passwordFetcher(password),
        firstName = firstName,
        lastName = lastName,
        bio = bio,
        role = roleFetcher()
    )
}