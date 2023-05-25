package com.aimelodies.services

import com.aimelodies.exceptions.genre.ExistingGenreException
import com.aimelodies.exceptions.genre.GenreNotFoundException
import com.aimelodies.models.requests.GenreRequest
import com.aimelodies.models.views.GenreView
import com.aimelodies.repositories.GenreRepository
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class GenreService(
    private val genreRepository: GenreRepository
) {

    fun findAll(): List<GenreView> =
        genreRepository.findAll().map { GenreView(it) }

    @Throws(ExistingGenreException::class)
    fun add(genreRequest: GenreRequest): GenreView {
        if (genreRepository.existsByNameIgnoreCase(genreRequest.name))
            throw ExistingGenreException()

        genreRepository.upsert(genreRequest.name)

        return GenreView(
            genreRepository.findByNameIgnoreCase(genreRequest.name)!!
        )
    }

    @Throws(ExistingGenreException::class, GenreNotFoundException::class)
    fun update(genreRequest: GenreRequest): GenreView = try {
        GenreView(
            genreRepository.update(genreRequest.toGenre()) ?: throw GenreNotFoundException()
        )
    } catch (dataAccessException: DataAccessException) {
        throw ExistingGenreException()
    }

    @Throws(GenreNotFoundException::class)
    fun delete(id: Long): Boolean {
        if (genreRepository.existsById(id).not())
            throw GenreNotFoundException()

        return try {
            genreRepository.delete(id)
        } catch (dataAccessException: DataAccessException) {
            false
        }
    }

    fun deleteUnused() = genreRepository.deleteUnused()
}