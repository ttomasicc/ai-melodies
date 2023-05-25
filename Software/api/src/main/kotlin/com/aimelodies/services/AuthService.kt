package com.aimelodies.services

import com.aimelodies.exceptions.artist.IllegalArtist
import com.aimelodies.exceptions.artist.IllegalArtistException
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.enumerations.Role
import com.aimelodies.models.requests.ArtistLoginRequest
import com.aimelodies.models.requests.ArtistRegistrationRequest
import com.aimelodies.models.views.ArtistView
import com.aimelodies.repositories.ArtistRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val artistRepository: ArtistRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    @Throws(IllegalArtistException::class)
    fun register(artistRegistrationRequest: ArtistRegistrationRequest): ArtistView {
        if (artistRepository.existsByUnameIgnoreCase(artistRegistrationRequest.username)) {
            throw IllegalArtistException(IllegalArtist.EXISTING_USERNAME.message)
        } else if (artistRepository.existsByEmailIgnoreCase(artistRegistrationRequest.email)) {
            throw IllegalArtistException(IllegalArtist.EXISTING_EMAIL_ADDRESS.message)
        }

        val artist = artistRepository.save(
            artistRegistrationRequest.toArtist(
                { Role.ARTIST },
                { password -> passwordEncoder.encode(password) }
            )
        )

        return ArtistView(artist)
    }

    @Transactional(readOnly = true)
    @Throws(AuthenticationException::class)
    fun login(artistLoginRequest: ArtistLoginRequest): Artist {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                artistLoginRequest.username,
                artistLoginRequest.password
            )
        )
        return findArtist(artistLoginRequest.username)!!
    }

    fun findArtist(username: String): Artist? =
        artistRepository.findByUnameIgnoreCase(username)
}