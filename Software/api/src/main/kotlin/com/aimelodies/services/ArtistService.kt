package com.aimelodies.services

import com.aimelodies.exceptions.artist.IllegalArtist
import com.aimelodies.exceptions.artist.IllegalArtistException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.views.ArtistView
import com.aimelodies.models.requests.ArtistUpdateRequest
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.repositories.filesystem.ImageFSRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArtistService(
    private val artistRepository: ArtistRepository,
    private val passwordEncoder: PasswordEncoder,
    private val imageFSRepository: ImageFSRepository
) {

    @Throws(ArtistNotFoundException::class)
    fun find(id: Long): ArtistView =
        ArtistView(
            artistRepository.findByIdOrNull(id) ?: throw ArtistNotFoundException()
        )

    @Transactional
    @Throws(ArtistNotFoundException::class, IllegalArtistException::class)
    fun update(artistUpdateRequest: ArtistUpdateRequest): ArtistView {
        val dbArtist = artistRepository.findByIdOrNull(artistUpdateRequest.id) ?: throw ArtistNotFoundException()
        if (dbArtist.uname != SecurityContextHolder.getContext().authentication.name)
            throw ArtistNotFoundException()

        artistUpdateRequest.email?.let {
            if (artistRepository.existsByEmailIgnoreCase(it))
                throw IllegalArtistException(IllegalArtist.EXISTING_EMAIL_ADDRESS.message)
        }

        var imagePath: String? = null
        artistUpdateRequest.image?.let {
            imagePath = imageFSRepository.save(it, dbArtist.image, Artist::class.java)
        }

        return ArtistView(
            artistRepository.save(update(dbArtist, artistUpdateRequest, imagePath))
        )
    }

    private fun update(
        artist: Artist,
        artistUpdateRequest: ArtistUpdateRequest,
        imagePath: String? = null
    ): Artist = artist.apply {
        email = artistUpdateRequest.email ?: artist.email
        passwd = artistUpdateRequest.password?.let { passwordEncoder.encode(it) } ?: artist.passwd
        firstName = artistUpdateRequest.firstName ?: artist.firstName
        lastName = artistUpdateRequest.lastName ?: artist.lastName
        bio = artistUpdateRequest.bio ?: artist.bio
        image = imagePath ?: artist.image
    }
}