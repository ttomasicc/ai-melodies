package com.aimelodies.models.views

import com.aimelodies.models.domain.Artist
import java.util.Date

data class ArtistView(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val bio: String?,
    val image: String?,
    val dateCreated: Date,
    val role: String
) {
    constructor(artist: Artist) : this(
        id = artist.id,
        username = artist.uname,
        email = artist.email,
        firstName = artist.firstName,
        lastName = artist.lastName,
        bio = artist.bio,
        image = artist.image,
        dateCreated = artist.dateCreated,
        role = artist.role.name
    )
}