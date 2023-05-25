package com.aimelodies.models.resources

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.hateoas.RepresentationModel
import java.util.Date

data class ArtistResource(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val bio: String?,
    val image: String?,
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="Europe/Zagreb")
    val dateCreated: Date,
    val role: String
) : RepresentationModel<ArtistResource>()