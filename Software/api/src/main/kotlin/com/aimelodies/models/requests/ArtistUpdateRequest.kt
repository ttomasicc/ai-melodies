package com.aimelodies.models.requests

import com.aimelodies.models.requests.validators.File
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class ArtistUpdateRequest(
    val id: Long = 0,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Size(max = 70, message = "The maximum email length is 70 characters")
    @field:Email(message = "Invalid email address")
    val email: String? = null,

    @field:NotBlank(message = "Password cannot be blank")
    val password: String? = null,

    @field:NotBlank(message = "First name cannot be blank")
    @field:Size(max = 70, message = "The maximum first name length is 70 characters")
    val firstName: String? = null,

    @field:NotBlank(message = "Last name cannot be blank")
    @field:Size(max = 70, message = "The maximum last name length is 70 characters")
    val lastName: String? = null,

    @field:NotBlank(message = "Biography cannot be blank")
    val bio: String? = null,

    @field:File(
        message = "Invalid graphics file - only jpg, jpeg and png files up to 1MB are allowed",
        allowedExtensions = ["jpg", "jpeg", "png"],
    )
    val image: MultipartFile? = null
)