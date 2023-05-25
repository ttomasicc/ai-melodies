package com.aimelodies.unit.services

import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.exceptions.artist.IllegalArtistException
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.enumerations.Role
import com.aimelodies.models.requests.ArtistUpdateRequest
import com.aimelodies.models.views.ArtistView
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.repositories.filesystem.ImageFSRepository
import com.aimelodies.services.ArtistService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Date

@ExtendWith(MockKExtension::class)
class ArtistServiceUnitTest {

    @MockK
    lateinit var artistRepository: ArtistRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var imageFSRepository: ImageFSRepository

    @InjectMockKs
    lateinit var artistService: ArtistService

    private val testArtist = Artist(
        id = 5,
        uname = "ttomasic",
        email = "ttomasic20@student.foi.hr",
        passwd = "admin",
        dateCreated = Date(),
        role = Role.ADMINISTRATOR,
        albums = mutableSetOf()
    )

    @BeforeEach
    fun setup() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken(testArtist.uname, null)
        testArtist.apply {
            email = "ttomasic20@student.foi.hr"
            passwd = "admin"
            firstName = "Tin"
            lastName = "Tomašić"
            bio = "Divan glazbeni život jednog životnog sladokusca!"
            image = "osobna.png"
        }
    }

    @Nested
    inner class FetchingSingleArtist() {
        @Test
        fun `fetching an artist that does not exist should throw ArtistNotFoundException`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id + 1)
            } returns null

            assertThrows<ArtistNotFoundException> {
                artistService.find(testArtist.id + 1)
            }
        }

        @Test
        fun `fetching an artist should return the artist`() {
            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThat(artistService.find(testArtist.id))
                .isEqualTo(ArtistView(testArtist))
        }
    }

    @Nested
    inner class UpdatingArtist {
        @Test
        fun `updating an artist that does not exist should throw ArtistNotFoundException`() {
            val artistUpdateRequest = ArtistUpdateRequest(id = testArtist.id)

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns null

            assertThrows<ArtistNotFoundException> {
                artistService.update(artistUpdateRequest)
            }
        }

        @Test
        fun `updating an artist different than the one currently logged in should throw ArtistNotFoundException`() {
            SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("elvis", null)
            val artistUpdateRequest = ArtistUpdateRequest(id = testArtist.id)

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist

            assertThrows<ArtistNotFoundException> {
                artistService.update(artistUpdateRequest)
            }
        }

        @Test
        fun `updating artist's email address onto an already existing email address should throw IllegalArtistException`() {
            val artistUpdateRequest = ArtistUpdateRequest(
                id = testArtist.id,
                email = testArtist.email
            )

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist
            every {
                artistRepository.existsByEmailIgnoreCase(artistUpdateRequest.email!!)
            } returns true

            assertThrows<IllegalArtistException> {
                artistService.update(artistUpdateRequest)
            }
            verify(exactly = 0) {
                artistRepository.save(any())
            }
        }

        @Test
        fun `updating artist's email address should return the artist with the updated email address`() {
            val artistUpdateRequest = ArtistUpdateRequest(
                id = testArtist.id,
                email = "ttomasic@foi.hr"
            )

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist
            every {
                artistRepository.existsByEmailIgnoreCase(artistUpdateRequest.email!!)
            } returns false
            every {
                artistRepository.save(
                    testArtist.apply { email = artistUpdateRequest.email!! }
                )
            } returns testArtist

            val artistView = artistService.update(artistUpdateRequest)

            assertThat(artistView)
                .isEqualTo(ArtistView(testArtist))
            assertThat(artistView.email)
                .isEqualTo(artistUpdateRequest.email)
            verify(exactly = 0) {
                imageFSRepository.save(any(), any(), Artist::class.java)
            }
        }

        @Test
        fun `updating artist's image should return the artist with the new image name`() {
            val newImageFile = MockMultipartFile("novi-ja.jpeg", null)
            val artistUpdateRequest = ArtistUpdateRequest(
                id = testArtist.id,
                image = newImageFile
            )
            val generatedImageName = "99f9201e-b228-11ed-afa1-0242ac120002.jpeg"

            every {
                artistRepository.findByIdOrNull(testArtist.id)
            } returns testArtist
            every {
                imageFSRepository.save(newImageFile, testArtist.image, Artist::class.java)
            } returns generatedImageName
            every {
                artistRepository.save(testArtist)
            } returns testArtist

            val artistView = artistService.update(artistUpdateRequest)
            assertThat(artistView)
                .isEqualTo(ArtistView(testArtist))
            assertThat(artistView.image)
                .isEqualTo(generatedImageName)
            verify(exactly = 0) {
                artistRepository.existsByEmailIgnoreCase(any())
            }
        }
    }
}