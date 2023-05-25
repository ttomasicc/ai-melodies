package com.aimelodies.unit.services

import com.aimelodies.exceptions.artist.IllegalArtist
import com.aimelodies.exceptions.artist.IllegalArtistException
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.enumerations.Role
import com.aimelodies.models.requests.ArtistLoginRequest
import com.aimelodies.models.requests.ArtistRegistrationRequest
import com.aimelodies.models.views.ArtistView
import com.aimelodies.repositories.ArtistRepository
import com.aimelodies.services.AuthService
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
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockKExtension::class)
class AuthServiceUnitTest {

    @MockK
    lateinit var artistRepository: ArtistRepository

    @MockK
    lateinit var authenticationManager: AuthenticationManager

    @MockK(relaxed = true)
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    lateinit var authService: AuthService

    @BeforeEach
    fun setup() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("ttomasic", "admin")
    }

    @Nested
    inner class Registration {
        @Test
        fun `registering an artist with the already existing username should throw IllegalArtistException`() {
            val artistRegistrationRequest = ArtistRegistrationRequest(
                username = "ttomasic"
            )

            every {
                artistRepository.existsByUnameIgnoreCase(artistRegistrationRequest.username)
            } returns true

            val exception = assertThrows<IllegalArtistException> {
                authService.register(artistRegistrationRequest)
            }
            assertThat(exception.message)
                .isEqualTo(IllegalArtist.EXISTING_USERNAME.message)
            verify(exactly = 0) {
                artistRepository.save(any())
            }
        }

        @Test
        fun `registering an artist with the already existing email address should throw IllegalArtistException`() {
            val artistRegistrationRequest = ArtistRegistrationRequest(
                username = "ttomasic",
                email = "ttomasic20@student.foi.hr"
            )

            every {
                artistRepository.existsByUnameIgnoreCase(artistRegistrationRequest.username)
            } returns false
            every {
                artistRepository.existsByEmailIgnoreCase(artistRegistrationRequest.email)
            } returns true

            val iznimka = assertThrows<IllegalArtistException> {
                authService.register(artistRegistrationRequest)
            }
            assertThat(iznimka.message)
                .isEqualTo(IllegalArtist.EXISTING_EMAIL_ADDRESS.message)
            verify(exactly = 0) {
                artistRepository.save(any())
            }
        }

        @Test
        fun `successful artist registration should return the newly created artist resource with the ARTIST role`() {
            val artistRegistrationRequest = ArtistRegistrationRequest(
                username = "ttomasic",
                email = "ttomasic20@student.foi.hr",
                password = "admin"
            )
            val artist = Artist(
                uname = artistRegistrationRequest.username,
                email = artistRegistrationRequest.email,
                passwd = passwordEncoder.encode(artistRegistrationRequest.password),
                role = Role.ARTIST
            )

            every {
                artistRepository.existsByUnameIgnoreCase(artistRegistrationRequest.username)
            } returns false
            every {
                artistRepository.existsByEmailIgnoreCase(artistRegistrationRequest.email)
            } returns false
            every {
                artistRepository.save(any())
            } returns artist

            val artistView = authService.register(artistRegistrationRequest)
            assertThat(artistView)
                .isEqualTo(ArtistView(artist))
            assertThat(artistView.role)
                .isEqualTo(Role.ARTIST.toString())
            verify(exactly = 1) {
                artistRepository.save(any())
            }
        }
    }

    @Nested
    inner class Login {
        @Test
        fun `trying to log in with the wrong username should throw AuthenticationException`() {
            val artistLoginRequest = ArtistLoginRequest(
                username = "elvis",
                password = "admin"
            )
            val authToken = TestingAuthenticationToken(
                artistLoginRequest.username,
                artistLoginRequest.password
            )

            every {
                authenticationManager.authenticate(authToken)
            } throws UsernameNotFoundException("")

            assertThrows<AuthenticationException> {
                authService.login(artistLoginRequest)
            }
        }

        @Test
        fun `successful login should return the authenticated artist`() {
            val artistLoginRequest = ArtistLoginRequest(
                username = "ttomasic",
                password = "admin"
            )
            val authToken = TestingAuthenticationToken(
                artistLoginRequest.username,
                artistLoginRequest.password
            )
            val artist = Artist(
                uname = artistLoginRequest.username,
                email = artistLoginRequest.username,
                passwd = passwordEncoder.encode(artistLoginRequest.password),
                role = Role.ARTIST
            )

            every {
                authenticationManager.authenticate(authToken)
            } returns authToken
            every {
                artistRepository.findByUnameIgnoreCase(artistLoginRequest.username)
            } returns artist

            assertThat(authService.login(artistLoginRequest))
                .isEqualTo(artist)
        }
    }
}