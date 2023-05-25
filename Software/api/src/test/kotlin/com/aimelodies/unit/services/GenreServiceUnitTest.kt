package com.aimelodies.unit.services

import com.aimelodies.exceptions.genre.ExistingGenreException
import com.aimelodies.exceptions.genre.GenreNotFoundException
import com.aimelodies.models.domain.Genre
import com.aimelodies.models.requests.GenreRequest
import com.aimelodies.models.views.GenreView
import com.aimelodies.repositories.GenreRepository
import com.aimelodies.services.GenreService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException

@ExtendWith(MockKExtension::class)
class GenreServiceUnitTest {

    @MockK
    lateinit var genreRepository: GenreRepository

    @InjectMockKs
    lateinit var genreService: GenreService

    @Nested
    inner class FetchingGenres {
        @Test
        fun `fetching all genres should return all genres`() {
            val genres = listOf(
                Genre(id = 1, "rockabilly"),
                Genre(id = 2, "blues"),
                Genre(id = 3, "jazz")
            )

            every {
                genreRepository.findAll()
            } returns genres

            assertThat(genreService.findAll())
                .isEqualTo(genres.map { GenreView(it) })
        }
    }

    @Nested
    inner class AddingGenre {
        @Test
        fun `adding existing genre should throw ExistingGenreException`() {
            val genreRequest = GenreRequest(name = "blues")

            every {
                genreRepository.existsByNameIgnoreCase(genreRequest.name)
            } returns true

            assertThrows<ExistingGenreException> {
                genreService.add(genreRequest)
            }
            verify(exactly = 0) {
                genreRepository.upsert(genreRequest.name)
            }
        }

        @Test
        fun `adding valid genre should return the newly created genre`() {
            val genreRequest = GenreRequest(name = "Blues")
            val genreDomain = Genre(id = 3, name = genreRequest.name.lowercase())
            val genreView = GenreView(genreDomain)

            every {
                genreRepository.existsByNameIgnoreCase(genreRequest.name)
            } returns false
            every {
                genreRepository.upsert(genreRequest.name)
            } returns Unit
            every {
                genreRepository.findByNameIgnoreCase(genreRequest.name)
            } returns genreDomain

            assertThat(genreService.add(genreRequest))
                .isEqualTo(genreView)
        }
    }

    @Nested
    inner class UpdatingGenre {
        @Test
        fun `updating genre that does not exist should throw GenreNotFoundException`() {
            val genreRequest = GenreRequest(name = "Blues")

            every {
                genreRepository.update(any())
            } returns null

            assertThrows<GenreNotFoundException> {
                genreService.update(genreRequest)
            }
        }

        @Test
        fun `updating genre name to an already existing genre name should throw ExistingGenreException`() {
            val genreRequest = GenreRequest(name = "Blues")

            every {
                genreRepository.update(any())
            } throws DuplicateKeyException("")

            assertThrows<ExistingGenreException> {
                genreService.update(genreRequest)
            }
        }

        @Test
        fun `updating genre should return the updated genre`() {
            val genreRequest = GenreRequest(name = "Blues")
            val genreDomain = Genre(id = 3, name = genreRequest.name.lowercase())
            val genreView = GenreView(genreDomain)

            every {
                genreRepository.update(any())
            } returns genreDomain

            assertThat(genreService.update(genreRequest))
                .isEqualTo(genreView)
        }
    }

    @Nested
    inner class DeletingGenres {
        @Test
        fun `deleting a genre that does not exist should throw GenreNotFoundException`() {
            val genreId = 5L

            every {
                genreRepository.existsById(genreId)
            } returns false

            assertThrows<GenreNotFoundException> {
                genreService.delete(genreId)
            }
            verify(exactly = 0) {
                genreRepository.delete(genreId)
            }
        }

        @Test
        fun `deleting a genre that has database references should return false`() {
            val genreId = 5L

            every {
                genreRepository.existsById(genreId)
            } returns true
            every {
                genreRepository.delete(genreId)
            } throws DataIntegrityViolationException("")

            assertFalse(genreService.delete(genreId))
        }
    }
}