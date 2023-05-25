package com.aimelodies.configurations

import com.aimelodies.repositories.ArtistRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfiguration(
    private val artistRepository: ArtistRepository
) {

    @Bean
    fun authenticationProvider(): AuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(userDetailsService())
        setPasswordEncoder(passwordEncoder())
    }

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
        artistRepository.findByUnameIgnoreCase(username) ?: throw UsernameNotFoundException("User not found")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}