package com.aimelodies.models.requests

import com.aimelodies.models.requests.validators.File
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class MelodyAddRequest(
    val authorId: Long = 0,
    val albumId: Long = 0,

    @field:Min(value = 1, message = "Genre id must be a number greater than 0")
    val genreId: Long = 0,

    @field:NotNull(message = "Missing audio file")
    @field:File(
        message = "Invalid audio file - only mp3, ogg and wav files up to 3MB are allowed",
        allowedExtensions = ["mp3", "ogg", "wav"],
        maximumBytes = 3 * 1024 * 1024
    )
    val audio: MultipartFile? = null,

    @field:NotBlank(message = "Melody name cannot be blank")
    @field:Size(max = 100, message = "Maximum melody name length is 100 characters")
    val name: String? = null,
)