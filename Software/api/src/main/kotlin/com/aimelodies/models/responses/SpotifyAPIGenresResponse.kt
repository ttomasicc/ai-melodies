package com.aimelodies.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyAPIGenresResponse(
    @JsonProperty("genres")
    val genres: List<String>
)