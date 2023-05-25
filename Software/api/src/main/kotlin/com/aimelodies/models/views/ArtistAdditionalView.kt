package com.aimelodies.models.views

import com.aimelodies.models.enumerations.ArtistAction

data class ArtistAdditionalView(
    val action: ArtistAction,
    val artist: ArtistView
)