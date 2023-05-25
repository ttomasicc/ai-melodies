package com.aimelodies.models.views

import com.aimelodies.models.domain.Genre

data class GenreView(
    val id: Long,
    val name: String
) {
    constructor(genre: Genre) : this(
        id = genre.id,
        name = genre.name
    )
}