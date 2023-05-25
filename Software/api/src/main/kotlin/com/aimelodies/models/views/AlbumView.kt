package com.aimelodies.models.views

import com.aimelodies.models.domain.Album
import java.util.Date

data class AlbumView(
    val id: Long,
    val title: String,
    val description: String?,
    val image: String?,
    val dateCreated: Date,
    val artist: ArtistView
) {

    constructor(album: Album) : this(
        id = album.id,
        title = album.title,
        description = album.description,
        image = album.image,
        dateCreated = album.dateCreated,
        artist = ArtistView(album.artist)
    )
}