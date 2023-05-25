package com.aimelodies.configurations

import com.aimelodies.configurations.settings.JwtSettings
import com.aimelodies.configurations.settings.SpotifySettings
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = [JwtSettings::class, SpotifySettings::class])
class SettingsConfiguration {

    @Bean
    fun jwtSettings(jwtSettings: JwtSettings) = jwtSettings

    @Bean
    fun spotifySettings(spotifySettings: SpotifySettings) = spotifySettings
}