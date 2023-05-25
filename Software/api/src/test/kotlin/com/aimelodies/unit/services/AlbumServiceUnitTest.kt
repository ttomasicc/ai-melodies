package com.aimelodies.unit.services

import com.aimelodies.exceptions.album.AlbumNotFoundException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.models.domain.Album
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.domain.Genre
import com.aimelodies.models.domain.Melody
import com.aimelodies.models.enumerations.Role
import com.aimelodies.models.requests.AlbumAddRequest
import com.aimelodies.models.requests.AlbumUpdateRequest
import com.aimelodies.models.views.AlbumView
import com.aimelodies.repositories.AlbumRepository
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.repositories.MelodyRepository
import com.aimelodies.repositories.filesystem.AudioFSRepository
import com.aimelodies.repositories.filesystem.ImageFSRepository
import com.aimelodies.services.AlbumService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockKExtension::class)
class AlbumServiceUnitTest {

    @MockK
    lateinit var albumRepository: AlbumRepository

    @MockK
    lateinit var artistRepository: ArtistRepository

    @MockK
    lateinit var melodyRepository: MelodyRepository

    @MockK(relaxed = true)
    lateinit var imageFSRepository: ImageFSRepository

    @MockK(relaxed = true)
    lateinit var audioFSRepository: AudioFSRepository

    @InjectMockKs
    lateinit var albumService: AlbumService

    private val testArtist = Artist(
        id = 5,
        uname = "ttomasic",
        email = "ttomasic20@student.foi.hr",
        passwd = "admin",
        role = Role.ADMINISTRATOR,
        albums = mutableSetOf()
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
        testArtist.albums.clear()
    }

    @Nested
    inner class FetchingAllAlbums {
        @Test
        fun `fetching all albums for an artists that does not exist should throw ArtistNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns null

            assertThrows<ArtistNotFoundException> {
                albumService.findAll(testArtist.id)
            }
        }

        @Test
        fun `fetching all albums for an artist that does not have any albums should return an empty list`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThat(albumService.findAll(testArtist.id))
                .isEmpty()
        }

        @Test
        fun `fetching all albums for a given artist should return a list with all albums`() {
            testArtist.albums.addAll(testAlbums)

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThat(albumService.findAll(testArtist.id))
                .isEqualTo(testArtist.albums.map { AlbumView(it) })
        }

        @Test
        fun `fetching an album page should return an album page`() {
            testArtist.albums.addAll(testAlbums)
            val pageImpl = PageImpl(testAlbums)

            every {
                albumRepository.findAll(Pageable.unpaged())
            } returns pageImpl

            assertThat(albumService.findAll(Pageable.unpaged()))
                .isEqualTo(pageImpl.map { AlbumView(it) })
        }
    }

    @Nested
    inner class SearchingAlbums {
        @Test
        fun `searching an album by title should return an album page with all albums that contain the given keyword in their title`() {
            testArtist.albums.addAll(testAlbums)
            val searchKeyword = "Kva"
            val pageImpl = PageImpl(testAlbums.filter { it.title.contains(searchKeyword) })

            every {
                albumRepository.findAllByTitleContainsIgnoreCase(searchKeyword, Pageable.unpaged())
            } returns pageImpl

            assertThat(albumService.search(searchKeyword, Pageable.unpaged()))
                .isEqualTo(pageImpl.map { AlbumView(it) })
        }
    }

    @Nested
    inner class FetchingSingleAlbum {
        @Test
        fun `fetching an album for an artist that does not exist should throw ArtistNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns null

            assertThrows<ArtistNotFoundException> {
                albumService.find(testArtist.id, 5L)
            }
        }

        @Test
        fun `fetching an album that does not exist should throw AlbumNotFoundException`() {
            testArtist.albums.addAll(testAlbums)
            val albumId = 10L

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<AlbumNotFoundException> {
                albumService.find(testArtist.id, albumId)
            }
        }

        @Test
        fun `fetching an album for a given artist should return the album`() {
            testArtist.albums.addAll(testAlbums)
            val albumId = 3L

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThat(albumService.find(testArtist.id, albumId))
                .isEqualTo(AlbumView(testAlbums.first { it.id == albumId }))
        }
    }

    @Nested
    inner class AddingAlbum {
        @Test
        fun `adding an album to an artist that does not exist should throw ArtistNotFoundException`() {
            val albumAddRequest = AlbumAddRequest(
                artistId = 5,
                title = "AI Simfonije"
            )

            every {
                artistRepository.findByIdOrNull(albumAddRequest.artistId)
            } returns null

            assertThrows<ArtistNotFoundException> {
                albumService.add(albumAddRequest)
            }
        }

        @Test
        fun `adding an album to a different artist should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val albumAddRequest = AlbumAddRequest(
                artistId = testArtist.id,
                title = "AI Simfonije"
            )

            every {
                artistRepository.findByIdOrNull(albumAddRequest.artistId)
            } returns testArtist

            assertThrows<ArtistNotFoundException> {
                albumService.add(albumAddRequest)
            }
        }

        @Test
        fun `adding an album should return the newly created album`() {
            val albumAddRequest = AlbumAddRequest(
                artistId = testArtist.id,
                title = "AI Simfonije"
            )
            val albumDomain = Album(
                id = 5,
                title = albumAddRequest.title!!,
                artist = testArtist
            )

            every {
                artistRepository.findByIdOrNull(albumAddRequest.artistId)
            } returns testArtist
            every {
                albumRepository.save(any())
            } returns albumDomain

            val albumView = albumService.add(albumAddRequest)

            assertAll(
                "albumView",
                { assertEquals(albumDomain.id, albumView.id) },
                { assertEquals(albumDomain.title, albumView.title) }
            )
        }
    }

    @Nested
    inner class UpdatingAlbum {
        @Test
        fun `updating an album that does not exist should throw AlbumNotFoundException`() {
            val albumUpdateRequest = AlbumUpdateRequest(
                albumId = 3,
                title = "AI Simfonije"
            )

            every {
                albumRepository.findByIdOrNull(albumUpdateRequest.albumId)
            } returns null

            assertThrows<AlbumNotFoundException> {
                albumService.update(albumUpdateRequest)
            }
            verify(exactly = 0) {
                albumRepository.save(any())
            }
        }

        @Test
        fun `updating an album that does not belong to the currently logged user should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val albumUpdateRequest = AlbumUpdateRequest(
                albumId = 3,
                title = "AI Simfonije"
            )

            every {
                albumRepository.findByIdOrNull(albumUpdateRequest.albumId)
            } returns testAlbums.first()

            assertThrows<ArtistNotFoundException> {
                albumService.update(albumUpdateRequest)
            }
            verify(exactly = 0) {
                albumRepository.save(any())
            }
        }

        @Test
        fun `updating album image should return the album with the new image name`() {
            val testAlbum = testAlbums.first()
            val oldAlbumImage = testAlbum.image
            val oldAlbumTitle = testAlbum.title

            val image = MockMultipartFile("jabuka.png", null)
            val albumUpdateRequest = AlbumUpdateRequest(
                albumId = 3,
                image = image
            )
            val newAlbumImageName = "nova-jabuka.png"

            every {
                albumRepository.findByIdOrNull(albumUpdateRequest.albumId)
            } returns testAlbum
            every {
                imageFSRepository.save(image, testAlbum.image, Album::class.java)
            } returns newAlbumImageName
            every {
                albumRepository.save(testAlbum)
            } returns testAlbum

            assertThat(albumService.update(albumUpdateRequest))
                .isEqualTo(AlbumView(testAlbum))
            assertThat(testAlbum.image)
                .isEqualTo(newAlbumImageName)
            assertThat(testAlbum.title)
                .isEqualTo(oldAlbumTitle)
            verify(exactly = 1) {
                imageFSRepository.save(image, null, Album::class.java)
            }

            testAlbum.image = oldAlbumImage
        }

        @Test
        fun `updating album title should return the album with the updated title`() {
            val testAlbum = testAlbums.first()
            val oldAlbumTitle = testAlbum.title
            val oldAlbumDescription = testAlbum.description

            val newAlbumTitle = "Cyber sonate"
            val albumUpdateRequest = AlbumUpdateRequest(
                albumId = 3,
                title = newAlbumTitle
            )

            every {
                albumRepository.findByIdOrNull(albumUpdateRequest.albumId)
            } returns testAlbum
            every {
                albumRepository.save(testAlbum)
            } returns testAlbum

            assertThat(albumService.update(albumUpdateRequest))
                .isEqualTo(AlbumView(testAlbum))
            assertThat(testAlbum.title)
                .isEqualTo(newAlbumTitle)
            assertThat(testAlbum.description)
                .isEqualTo(oldAlbumDescription)
            verify(exactly = 0) {
                imageFSRepository.save(any(), any(), Album::class.java)
            }

            testAlbum.title = oldAlbumTitle
        }
    }

    @Nested
    inner class DeletingAlbum {
        @Test
        fun `deleting an album that does not exist should throw AlbumNotFoundException`() {
            val testAlbum = testAlbums.first()

            every {
                albumRepository.findByIdOrNull(testAlbum.id)
            } returns null

            assertThrows<AlbumNotFoundException> {
                albumService.delete(testAlbum.id)
            }
        }

        @Test
        fun `deleting an album that does not belong to the currently logged user should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val testAlbum = testAlbums.first()

            every {
                albumRepository.findByIdOrNull(testAlbum.id)
            } returns testAlbum

            assertThrows<ArtistNotFoundException> {
                albumService.delete(testAlbum.id)
            }
        }

        @Test
        fun `deleting an album should also delete all the melodies that were referenced only by the given album`() {
            val testAlbum = testAlbums.first()
            val melody1 = Melody(
                id = 1,
                audio = "",
                author = testArtist,
                genre = Genre(name = "blues"),
                albums = mutableSetOf(testAlbum, testAlbums[1])
            )
            val melody2 = Melody(
                id = 2,
                audio = "",
                author = testArtist,
                genre = Genre(name = "blues"),
                albums = mutableSetOf(testAlbum)
            )
            testAlbum.melodies.addAll(listOf(melody1, melody2))

            every {
                albumRepository.findByIdOrNull(testAlbum.id)
            } returns testAlbum
            every {
                melodyRepository.deleteById(melody2.id)
            } returns Unit
            every {
                albumRepository.deleteById(testAlbum.id)
            } returns Unit

            albumService.delete(testAlbum.id)
            verify(exactly = 1) {
                melodyRepository.deleteById(any())
                audioFSRepository.delete(any(), Melody::class.java)
                albumRepository.deleteById(testAlbum.id)
            }

            testAlbum.melodies.clear()
        }

        @Test
        fun `deleting an album should also delete its image`() {
            val testAlbum = testAlbums.first()
            val oldAlbumImageName = testAlbum.image

            val albumImageName = "test.png"
            testAlbum.image = albumImageName

            every {
                albumRepository.findByIdOrNull(testAlbum.id)
            } returns testAlbum
            every {
                albumRepository.deleteById(testAlbum.id)
            } returns Unit

            albumService.delete(testAlbum.id)
            verify(exactly = 1) {
                imageFSRepository.delete(albumImageName, Album::class.java)
            }

            testAlbum.image = oldAlbumImageName
        }
    }
}