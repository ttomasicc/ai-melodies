package com.aimelodies.controllers.api

import com.aimelodies.configurations.settings.JwtSettings
import com.aimelodies.controllers.ControllerValidator
import com.aimelodies.controllers.assemblers.AuthResourceAssembler
import com.aimelodies.controllers.assemblers.JwtResourceAssembler
import com.aimelodies.exceptions.artist.IllegalArtistException
import com.aimelodies.models.enumerations.ArtistAction
import com.aimelodies.models.requests.ArtistLoginRequest
import com.aimelodies.models.requests.ArtistRegistrationRequest
import com.aimelodies.models.resources.ArtistResource
import com.aimelodies.models.resources.JwtResource
import com.aimelodies.models.views.ArtistAdditionalView
import com.aimelodies.models.views.ArtistView
import com.aimelodies.models.views.JwtView
import com.aimelodies.services.AuthService
import com.aimelodies.services.JwtService
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/auth"])
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService,
    private val jwtSettings: JwtSettings,
    private val authResourceAssembler: AuthResourceAssembler,
    private val jwtResourceAssembler: JwtResourceAssembler,
    private val validator: ControllerValidator
) {

    companion object : KLogging()

    @GetMapping(path = ["/token"])
    fun getToken(
        session: HttpSession
    ): ResponseEntity<JwtResource> {
        val sessionJwt = session.getAttribute(jwtSettings.sessionKey) as String?

        sessionJwt?.let {
            if (jwtService.isValid(it, allowedExceptions = arrayOf(TokenExpiredException::class.java))) {
                val artist = authService.findArtist(jwtService.getUsername(it))!!

                val jwt = jwtService.generate(artist)
                session.setAttribute(jwtSettings.sessionKey, jwt)

                return ResponseEntity.ok(
                    jwtResourceAssembler.toModel(JwtView(jwt))
                )
            }
        }

        return ResponseEntity.ok(
            jwtResourceAssembler.toModel(JwtView(null))
        )
    }

    @GetMapping(path = ["/current"])
    fun getCurrent(): ResponseEntity<ArtistResource> =
        ResponseEntity.ok(
            authResourceAssembler.toModel(
                ArtistAdditionalView(
                    action = ArtistAction.LOGIN,
                    artist = ArtistView(
                        authService.findArtist(
                            SecurityContextHolder.getContext().authentication.principal as String
                        )!!
                    )
                )
            )
        )

    @PostMapping(path = ["/login"])
    fun login(
        @Valid @RequestBody artistLoginRequest: ArtistLoginRequest,
        bindingResult: BindingResult,
        session: HttpSession
    ): ResponseEntity<ArtistResource> = try {
        validator.verifyErrors(bindingResult)

        val artist = authService.login(artistLoginRequest)

        val jwt = jwtService.generate(artist)
        session.setAttribute(jwtSettings.sessionKey, jwt)

        ResponseEntity.ok(
            authResourceAssembler.toModel(
                ArtistAdditionalView(
                    action = ArtistAction.LOGIN,
                    artist = ArtistView(artist)
                )
            )
        )
    } catch (illegalArtistException: IllegalArtistException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, illegalArtistException.message, illegalArtistException)
    }

    @PostMapping(path = ["/register"])
    fun register(
        @Valid @RequestBody artistRegistrationRequest: ArtistRegistrationRequest,
        bindingResult: BindingResult,
        session: HttpSession
    ): ResponseEntity<ArtistResource> = try {
        validator.verifyNonNullErrors(bindingResult)

        val artistResource = authResourceAssembler.toModel(
            ArtistAdditionalView(
                action = ArtistAction.REGISTRATION,
                artist = authService.register(artistRegistrationRequest)
            )
        )

        session.invalidate()

        ResponseEntity(artistResource, HttpStatus.CREATED)
    } catch (illegalArtistException: IllegalArtistException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, illegalArtistException.message, illegalArtistException)
    }

    @DeleteMapping(path = ["/logout"])
    fun logout(
        session: HttpSession
    ): ResponseEntity<Unit> = try {
        session.invalidate()
        ResponseEntity.noContent().build()
    } catch (illegalStateException: java.lang.IllegalStateException) {
        ResponseEntity.noContent().build()
    }
}