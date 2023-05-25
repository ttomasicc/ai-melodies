package com.aimelodies.models.views

import com.aimelodies.models.domain.Melody
import java.util.Date

data class MelodyView(
    val id: Long,
    val audio: String,
    val name: String,
    val dateCreated: Date,
    val author: ArtistView,
    val genre: GenreView,
    val albums: List<AlbumView>
) {

    constructor(melody: Melody) : this(
        id = melody.id,
        audio = melody.audio,
        name = melody.name,
        dateCreated = melody.dateCreated,
        author = ArtistView(melody.author),
        genre = GenreView((melody.genre)),
        albums = melody.albums.map { AlbumView(it) }
    )
}