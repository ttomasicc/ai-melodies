package com.aimelodies.services

import com.aimelodies.exceptions.album.AlbumNotFoundException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.exceptions.genre.GenreNotFoundException
import com.aimelodies.exceptions.melody.MelodyNotFoundException
import com.aimelodies.models.domain.Melody
import com.aimelodies.models.requests.MelodyAddRequest
import com.aimelodies.models.requests.MelodyUpdateRequest
import com.aimelodies.models.views.MelodyView
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.repositories.filesystem.AudioFSRepository
import com.aimelodies.repositories.GenreRepository
import com.aimelodies.repositories.MelodyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MelodyService(
    private val artistRepository: ArtistRepository,
    private val melodyRepository: MelodyRepository,
    private val genreRepository: GenreRepository,
    private val audioFSRepository: AudioFSRepository
) {

    @Transactional(readOnly = true)
    @Throws(ArtistNotFoundException::class, AlbumNotFoundException::class)
    fun findAll(artistId: Long, albumId: Long): List<MelodyView> {
        val artist = artistRepository.findByIdOrNull(artistId) ?: throw ArtistNotFoundException()
        val album = artist.albums.firstOrNull { it.id == albumId } ?: throw AlbumNotFoundException()
        return album.melodies.map { MelodyView(it) }
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<MelodyView> =
        melodyRepository.findAll(pageable).map { MelodyView(it) }

    @Transactional(readOnly = true)
    fun search(searchQuery: String, pageable: Pageable): Page<MelodyView> =
        melodyRepository.findAllByNameContainsIgnoreCase(searchQuery, pageable).map { MelodyView(it) }

    @Transactional(readOnly = true)
    @Throws(ArtistNotFoundException::class, AlbumNotFoundException::class, MelodyNotFoundException::class)
    fun find(artistId: Long, albumId: Long, melodyId: Long): MelodyView {
        val artist = artistRepository.findByIdOrNull(artistId) ?: throw ArtistNotFoundException()
        val album = artist.albums.firstOrNull { it.id == albumId } ?: throw AlbumNotFoundException()
        return MelodyView(
            album.melodies.firstOrNull { it.id == melodyId } ?: throw MelodyNotFoundException()
        )
    }

    @Transactional
    @Throws(ArtistNotFoundException::class, AlbumNotFoundException::class, GenreNotFoundException::class)
    fun add(melodyAddRequest: MelodyAddRequest): MelodyView {
        val artist = artistRepository.findByIdOrNull(
            melodyAddRequest.authorId
        ) ?: throw ArtistNotFoundException()
        authenticate(artist.uname)

        val album = artist.albums.firstOrNull { it.id == melodyAddRequest.albumId } ?: throw AlbumNotFoundException()

        val melody = Melody(
            genre = genreRepository.findByIdOrNull(melodyAddRequest.genreId) ?: throw GenreNotFoundException(),
            audio = audioFSRepository.save(melodyAddRequest.audio!!, type = Melody::class.java),
            author = artist,
            albums = mutableSetOf(album)
        ).also { melody ->
            melodyAddRequest.name?.let {
                melody.name = it
            }
        }

        album.melodies.add(melody)

        return MelodyView(
            melodyRepository.save(melody)
        )
    }

    @Transactional
    @Throws(ArtistNotFoundException::class, AlbumNotFoundException::class, MelodyNotFoundException::class)
    fun update(melodyUpdateRequest: MelodyUpdateRequest): MelodyView {
        val artist = artistRepository.findByIdOrNull(
            melodyUpdateRequest.artistId
        ) ?: throw ArtistNotFoundException()
        authenticate(artist.uname)

        val album = artist.albums.firstOrNull {
            it.id == melodyUpdateRequest.albumId
        } ?: throw AlbumNotFoundException()
        val melody = album.melodies.firstOrNull {
            it.id == melodyUpdateRequest.melodyId
        } ?: throw MelodyNotFoundException()

        melody.name = melodyUpdateRequest.name

        return MelodyView(
            melodyRepository.save(melody)
        )
    }

    @Transactional
    @Throws(ArtistNotFoundException::class, AlbumNotFoundException::class, MelodyNotFoundException::class)
    fun delete(artistId: Long, albumId: Long, melodyId: Long) {
        val artist = artistRepository.findByIdOrNull(artistId) ?: throw ArtistNotFoundException()
        authenticate(artist.uname)

        val album = artist.albums.firstOrNull { it.id == albumId } ?: throw AlbumNotFoundException()
        val melody = album.melodies.firstOrNull { it.id == melodyId } ?: throw MelodyNotFoundException()

        audioFSRepository.delete(melody.audio, Melody::class.java)
        melodyRepository.delete(melody)
    }

    @Throws(ArtistNotFoundException::class)
    private fun authenticate(username: String) {
        if (username != SecurityContextHolder.getContext().authentication.principal)
            throw ArtistNotFoundException()
    }
}