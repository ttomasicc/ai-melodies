package com.aimelodies.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyAPITokenResponse(
    @JsonProperty("access_token")
    val token: String
)