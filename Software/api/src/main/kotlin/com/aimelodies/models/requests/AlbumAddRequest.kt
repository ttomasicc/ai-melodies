package com.aimelodies.models.requests

import com.aimelodies.models.domain.Album
import com.aimelodies.models.domain.Artist
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AlbumAddRequest(
    val artistId: Long = 0,

    @field:NotBlank(message = "Album title cannot be blank")
    @field:Size(max = 100, message = "The maximum album title length is 100 characters")
    val title: String? = null,

    @field:NotBlank(message = "Album description cannot be blank")
    val description: String? = null,
) {

    fun toAlbum(
        artistFetcher: (id: Long) -> Artist
    ) = Album(
        description = description,
        artist = artistFetcher(artistId),
    ).also { album ->
        title?.let {
            album.title = it
        }
    }
}