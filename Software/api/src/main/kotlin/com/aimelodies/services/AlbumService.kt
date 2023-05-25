package com.aimelodies.services

import com.aimelodies.exceptions.album.AlbumNotFoundException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.models.domain.Album
import com.aimelodies.models.domain.Melody
import com.aimelodies.models.views.AlbumView
import com.aimelodies.models.requests.AlbumUpdateRequest
import com.aimelodies.models.requests.AlbumAddRequest
import com.aimelodies.repositories.AlbumRepository
import com.aimelodies.repositories.MelodyRepository
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.repositories.filesystem.AudioFSRepository
import com.aimelodies.repositories.filesystem.ImageFSRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlbumService(
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val melodyRepository: MelodyRepository,
    private val imageFSRepository: ImageFSRepository,
    private val audioFSRepository: AudioFSRepository
) {

    @Transactional(readOnly = true)
    @Throws(ArtistNotFoundException::class)
    fun findAll(artistId: Long): List<AlbumView> {
        val artist = artistRepository.findByIdOrNull(artistId) ?: throw ArtistNotFoundException()
        return artist.albums.map { AlbumView(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<AlbumView> =
        albumRepository.findAll(pageable).map { AlbumView(it) }

    @Transactional(readOnly = true)
    fun search(searchQuery: String, pageable: Pageable): Page<AlbumView> =
        albumRepository.findAllByTitleContainsIgnoreCase(searchQuery, pageable).map { AlbumView(it) }

    @Transactional(readOnly = true)
    @Throws(ArtistNotFoundException::class, AlbumNotFoundException::class)
    fun find(artistId: Long, albumId: Long): AlbumView {
        val artist = artistRepository.findByIdOrNull(artistId) ?: throw ArtistNotFoundException()
        return AlbumView(
            artist.albums.firstOrNull { it.id == albumId } ?: throw AlbumNotFoundException()
        )
    }

    @Throws(ArtistNotFoundException::class)
    fun add(albumAddRequest: AlbumAddRequest): AlbumView =
        AlbumView(
            albumRepository.save(
                albumAddRequest.toAlbum { artistId ->
                    (artistRepository.findByIdOrNull(artistId) ?: throw ArtistNotFoundException()).also {
                        authenticate(it.uname)
                    }
                }
            )
        )

    @Transactional
    @Throws(AlbumNotFoundException::class)
    fun update(albumUpdateRequest: AlbumUpdateRequest): AlbumView {
        val dbAlbum = albumRepository.findByIdOrNull(albumUpdateRequest.albumId) ?: throw AlbumNotFoundException()
        authenticate(dbAlbum.artist.uname)

        var imagePath: String? = null
        albumUpdateRequest.image?.let {
            imagePath = imageFSRepository.save(it, dbAlbum.image, Album::class.java)
        }

        return AlbumView(
            albumRepository.save(update(dbAlbum, albumUpdateRequest, imagePath))
        )
    }

    @Transactional
    @Throws(AlbumNotFoundException::class)
    fun delete(albumId: Long) {
        val album = albumRepository.findByIdOrNull(albumId) ?: throw AlbumNotFoundException()
        authenticate(album.artist.uname)

        album.melodies.forEach { melody ->
            if (melody.albums.size == 1) {
                melodyRepository.deleteById(melody.id)
                audioFSRepository.delete(melody.audio, Melody::class.java)
            }
        }

        album.image?.let {
            imageFSRepository.delete(it, Album::class.java)
        }

        albumRepository.deleteById(album.id)
    }

    private fun update(
        album: Album,
        albumUpdateRequest: AlbumUpdateRequest,
        imagePath: String? = null
    ): Album = album.apply {
        title = albumUpdateRequest.title ?: album.title
        description = albumUpdateRequest.description ?: album.description
        image = imagePath ?: album.image
    }

    @Throws(ArtistNotFoundException::class)
    private fun authenticate(username: String) {
        if (username != SecurityContextHolder.getContext().authentication.principal)
            throw ArtistNotFoundException()
    }
}