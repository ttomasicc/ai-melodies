package com.aimelodies.models.requests

import com.aimelodies.models.requests.validators.File
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class AlbumUpdateRequest(
    val albumId: Long = 0,

    @field:NotBlank(message = "Album title cannot be blank")
    @field:Size(max = 100, message = "The maximum album title length is 100 characters")
    val title: String? = null,

    @field:NotBlank(message = "Album description cannot be blank")
    val description: String? = null,

    @field:File(
        message = "Invalid graphics file - only jpg, jpeg and png files up to 1MB are allowed",
        allowedExtensions = ["jpg", "jpeg", "png"],
    )
    val image: MultipartFile? = null
)