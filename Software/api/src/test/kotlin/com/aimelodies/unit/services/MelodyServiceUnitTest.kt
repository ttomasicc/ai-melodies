package com.aimelodies.unit.services

import com.aimelodies.exceptions.album.AlbumNotFoundException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.exceptions.genre.GenreNotFoundException
import com.aimelodies.exceptions.melody.MelodyNotFoundException
import com.aimelodies.models.domain.Album
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.domain.Genre
import com.aimelodies.models.domain.Melody
import com.aimelodies.models.enumerations.Role
import com.aimelodies.models.requests.MelodyAddRequest
import com.aimelodies.models.requests.MelodyUpdateRequest
import com.aimelodies.models.views.MelodyView
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.repositories.filesystem.AudioFSRepository
import com.aimelodies.repositories.GenreRepository
import com.aimelodies.repositories.MelodyRepository
import com.aimelodies.services.MelodyService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockKExtension::class)
class MelodyServiceUnitTest {

    @MockK
    lateinit var artistRepository: ArtistRepository

    @MockK
    lateinit var melodyRepository: MelodyRepository

    @MockK
    lateinit var genreRepository: GenreRepository

    @MockK(relaxed = true)
    lateinit var audioFSRepository: AudioFSRepository

    @InjectMockKs
    lateinit var melodyService: MelodyService

    private val testArtist = Artist(
        id = 5,
        uname = "ttomasic",
        email = "ttomasic20@student.foi.hr",
        passwd = "admin",
        role = Role.ADMINISTRATOR,
    )

    private val testGenres = listOf(
        Genre(id = 1, name = "blues"),
        Genre(id = 2, name = "jazz")
    )

    private val testMelodies = listOf(
        Melody(
            id = 1,
            audio = "sanjalica.wav",
            name = "AI sanjar",
            author = testArtist,
            genre = testGenres[0],
        ),
        Melody(
            id = 2,
            audio = "kvant.ogg",
            name = "Mrežni valovi",
            author = testArtist,
            genre = testGenres[1],
        ),
        Melody(
            id = 3,
            audio = "Algoritamska sonata.mp3",
            name = "Algoritamska sonata",
            author = testArtist,
            genre = testGenres[0],
        ),
        Melody(
            id = 4,
            audio = "mv.mp3",
            name = "Mehanički valcer",
            author = testArtist,
            genre = testGenres[1],
        )
    )

    private val testAlbums = listOf(
        Album(id = 1, title = "Algoritamske ode", artist = testArtist),
        Album(id = 2, title = "Kvantna glazba", artist = testArtist),
        Album(id = 3, title = "Digitalne fantazije", artist = testArtist),
        Album(id = 4, title = "Kvarkovi", artist = testArtist)
    )

    @BeforeEach
    fun setup() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken(testArtist.uname, null)
        testAlbums.forEach { album ->
            album.melodies.add(testMelodies[album.id.toInt() - 1])
        }
        testArtist.albums.addAll(testAlbums)
    }

    @AfterEach
    fun teardown() {
        testAlbums.forEach { it.melodies.clear() }
        testArtist.albums.clear()
    }

    @Nested
    inner class FetchingAllMelodies {
        @Test
        fun `fetching all album melodies for an artist that does not exist should throw ArtistNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns null

            assertThrows<ArtistNotFoundException> {
                melodyService.findAll(testArtist.id, testAlbums.last().id)
            }
        }

        @Test
        fun `fetching all album melodies for an album that does not exist should throw AlbumNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<AlbumNotFoundException> {
                melodyService.findAll(testArtist.id, testArtist.albums.last().id + 1)
            }
        }

        @Test
        fun `fetching all album melodies for a given artist should return the list with all melodies`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThat(melodyService.findAll(testArtist.id, testAlbums.first().id))
                .isEqualTo(testAlbums.first().melodies.map { MelodyView(it) })
        }

        @Test
        fun `fetching a melody page should return a melody page`() {
            val pageImpl = PageImpl(testMelodies)

            every {
                melodyRepository.findAll(Pageable.unpaged())
            } returns pageImpl

            assertThat(melodyService.findAll(Pageable.unpaged()))
                .isEqualTo(pageImpl.map { MelodyView(it) })
        }
    }

    @Nested
    inner class SearchingMelodies() {
        @Test
        fun `searching a melody by name should return a melody page with all melodies that contain the given keyword in their name`() {
            val searchKeyword = "val"
            val pageImpl = PageImpl(testMelodies.filter { it.name.contains(searchKeyword) })

            every {
                melodyRepository.findAllByNameContainsIgnoreCase(searchKeyword, Pageable.unpaged())
            } returns pageImpl

            assertThat(melodyService.search(searchKeyword, Pageable.unpaged()))
                .isEqualTo(pageImpl.map { MelodyView(it) })
        }
    }

    @Nested
    inner class FetchingSingleMelody {
        @Test
        fun `fetching a melody for an artist that does not exist should throw ArtistNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns null

            assertThrows<ArtistNotFoundException> {
                melodyService.find(testArtist.id, testAlbums.last().id, testMelodies.first().id)
            }
        }

        @Test
        fun `fetching a melody within an album that does not exist should throw AlbumNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<AlbumNotFoundException> {
                melodyService.find(testArtist.id, testArtist.albums.last().id + 1, testMelodies.first().id)
            }
        }

        @Test
        fun `fetching a melody that does not exist should throw MelodyNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<MelodyNotFoundException> {
                melodyService.find(
                    testArtist.id,
                    testArtist.albums.last().id,
                    testArtist.albums.last().melodies.last().id + 1
                )
            }
        }

        @Test
        fun `fetching a melody within an album for a given artist should return the melody`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            val melodyView = melodyService.find(
                testArtist.id,
                testArtist.albums.last().id,
                testArtist.albums.last().melodies.first().id
            )
            assertThat(melodyView)
                .isEqualTo(MelodyView(testArtist.albums.last().melodies.first()))
        }
    }

    @Nested
    inner class AddingMelodies {
        @Test
        fun `adding a melody to an artist that does not exist should throw ArtistNotFoundException`() {
            val melodyAddRequest = MelodyAddRequest(
                authorId = testArtist.id
            )

            every {
                artistRepository.findByIdOrNull(melodyAddRequest.authorId)
            } returns null

            assertThrows<ArtistNotFoundException> {
                melodyService.add(melodyAddRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `adding a melody to an artist different than the one currently logged in should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val melodijaZahtjev = MelodyAddRequest(
                authorId = testArtist.id
            )

            every {
                artistRepository.findByIdOrNull(melodijaZahtjev.authorId)
            } returns testArtist

            assertThrows<ArtistNotFoundException> {
                melodyService.add(melodijaZahtjev)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `adding a melody to an album that does not exist should throw AlbumNotFoundException`() {
            val melodyAddRequest = MelodyAddRequest(
                authorId = testArtist.id,
                albumId = testArtist.albums.last().id + 1
            )

            every {
                artistRepository.findByIdOrNull(melodyAddRequest.authorId)
            } returns testArtist

            assertThrows<AlbumNotFoundException> {
                melodyService.add(melodyAddRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `adding melody with a genre that does not exist should throw GenreNotFoundException`() {
            val melodyAddRequest = MelodyAddRequest(
                authorId = testArtist.id,
                albumId = testArtist.albums.last().id,
                genreId = testGenres.last().id + 1
            )

            every {
                artistRepository.findByIdOrNull(melodyAddRequest.authorId)
            } returns testArtist
            every {
                genreRepository.findByIdOrNull(melodyAddRequest.genreId)
            } returns null

            assertThrows<GenreNotFoundException> {
                melodyService.add(melodyAddRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `adding a melody to an album should return the melody that belongs to the given album`() {
            val melodyAddRequest = MelodyAddRequest(
                authorId = testArtist.id,
                albumId = testArtist.albums.last().id,
                audio = MockMultipartFile("syntth.wav", null),
                genreId = testGenres.last().id,
                name = "Sinetički šapat"
            )
            val melodyDomain = Melody(
                genre = testGenres.last(),
                audio = "syntth.wav",
                author = testArtist,
                albums = mutableSetOf(testArtist.albums.last())
            )

            every {
                artistRepository.findByIdOrNull(melodyAddRequest.authorId)
            } returns testArtist
            every {
                genreRepository.findByIdOrNull(melodyAddRequest.genreId)
            } returns testGenres.last()
            every {
                melodyRepository.save(any())
            } returns melodyDomain

            val melodyView = melodyService.add(melodyAddRequest)
            assertThat(melodyView)
                .isEqualTo(MelodyView(melodyDomain))
            assertThat(melodyView.albums.size)
                .isEqualTo(1L)
            assertThat(testArtist.albums.last().melodies.size)
                .isEqualTo(2L)
            verify(exactly = 1) {
                melodyRepository.save(any())
            }
        }
    }

    @Nested
    inner class UpdatingMelody {
        @Test
        fun `updating a melody for an artist that does not exist should throw ArtistNotFoundException`() {
            val melodyUpdateRequest = MelodyUpdateRequest(
                artistId = testArtist.id
            )

            every {
                artistRepository.findByIdOrNull(melodyUpdateRequest.artistId)
            } returns null

            assertThrows<ArtistNotFoundException> {
                melodyService.update(melodyUpdateRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `updating a melody belonging to an artist different than the one currently logged in should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val melodyUpdateRequest = MelodyUpdateRequest(
                artistId = testArtist.id
            )

            every {
                artistRepository.findByIdOrNull(melodyUpdateRequest.artistId)
            } returns testArtist

            assertThrows<ArtistNotFoundException> {
                melodyService.update(melodyUpdateRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `updating a melody that does not exist within the given album should throw AlbumNotFoundException`() {
            val melodyUpdateRequest = MelodyUpdateRequest(
                artistId = testArtist.id,
                albumId = testArtist.albums.last().id + 1
            )

            every {
                artistRepository.findByIdOrNull(melodyUpdateRequest.artistId)
            } returns testArtist

            assertThrows<AlbumNotFoundException> {
                melodyService.update(melodyUpdateRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `updating a melody that does not exist should throw MelodyNotFoundException`() {
            val melodyUpdateRequest = MelodyUpdateRequest(
                artistId = testArtist.id,
                albumId = testArtist.albums.last().id,
                melodyId = testArtist.albums.last().melodies.last().id + 1
            )

            every {
                artistRepository.findByIdOrNull(melodyUpdateRequest.artistId)
            } returns testArtist

            assertThrows<MelodyNotFoundException> {
                melodyService.update(melodyUpdateRequest)
            }
            verify(exactly = 0) {
                melodyRepository.save(any())
            }
        }

        @Test
        fun `updating a melody name should return the melody with the updated name`() {
            val testMelody = testArtist.albums.last().melodies.last()
            val oldName = testMelody.name
            val newName = "Retro bitovi"
            val melodyUpdateRequest = MelodyUpdateRequest(
                artistId = testArtist.id,
                albumId = testArtist.albums.last().id,
                melodyId = testMelody.id,
                name = newName
            )

            every {
                artistRepository.findByIdOrNull(melodyUpdateRequest.artistId)
            } returns testArtist
            every {
                melodyRepository.save(testMelody)
            } returns testMelody

            val melodyView = melodyService.update(melodyUpdateRequest)
            assertThat(melodyView)
                .isEqualTo(MelodyView(testMelody))
            assertThat(melodyView.name)
                .isEqualTo(newName)
            verify(exactly = 1) {
                melodyRepository.save(any())
            }

            testMelody.name = oldName
        }
    }

    @Nested
    inner class DeletingMelodies {
        @Test
        fun `deleting a melody for an artist that does not exist should throw ArtistNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns null

            assertThrows<ArtistNotFoundException> {
                melodyService.delete(
                    testArtist.id,
                    testArtist.albums.last().id,
                    testArtist.albums.last().melodies.last().id
                )
            }
            verify(exactly = 0) {
                audioFSRepository.delete(any(), Melody::class.java)
                melodyRepository.delete(any())
            }
        }

        @Test
        fun `deleting a melody that belongs to an artist different than the one currently logged in should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<ArtistNotFoundException> {
                melodyService.delete(
                    testArtist.id,
                    testArtist.albums.last().id,
                    testArtist.albums.last().melodies.last().id
                )
            }
            verify(exactly = 0) {
                audioFSRepository.delete(any(), Melody::class.java)
                melodyRepository.delete(any())
            }
        }

        @Test
        fun `deleting a melody for an album that does not exist should throw AlbumNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<AlbumNotFoundException> {
                melodyService.delete(
                    testArtist.id,
                    testArtist.albums.last().id + 1,
                    testArtist.albums.last().melodies.last().id
                )
            }
            verify(exactly = 0) {
                audioFSRepository.delete(any(), Melody::class.java)
                melodyRepository.delete(any())
            }
        }

        @Test
        fun `deleting a melody that does not exist should throw MelodyNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<MelodyNotFoundException> {
                melodyService.delete(
                    testArtist.id,
                    testArtist.albums.last().id,
                    testArtist.albums.last().melodies.last().id + 1
                )
            }
            verify(exactly = 0) {
                audioFSRepository.delete(any(), Melody::class.java)
                melodyRepository.delete(any())
            }
        }

        @Test
        fun `deleting a melody should delete the melody from all referenced albums, along with its audio file`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist
            every {
                melodyRepository.delete(testArtist.albums.last().melodies.last())
            } returns Unit

            melodyService.delete(
                testArtist.id,
                testArtist.albums.last().id,
                testArtist.albums.last().melodies.last().id
            )

            verify(exactly = 1) {
                audioFSRepository.delete(any(), Melody::class.java)
                melodyRepository.delete(any())
            }
        }
    }
}