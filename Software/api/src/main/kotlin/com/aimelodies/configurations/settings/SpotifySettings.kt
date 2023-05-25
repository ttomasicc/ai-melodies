package com.aimelodies.configurations.settings

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.spotify")
data class SpotifySettings @ConstructorBinding constructor(
    @field:NotBlank(message = "Missing Spotify client secret")
    val baseUrl: String,
    @field:NotBlank(message = "Missing Spotify client ID")
    val clientId: String,
    @field:NotBlank(message = "Missing Spotify client secret")
    val clientSecret: String
)