package com.aimelodies.controllers.api

import com.aimelodies.controllers.ControllerValidator
import com.aimelodies.controllers.assemblers.ArtistResourceAssembler
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.exceptions.artist.IllegalArtistException
import com.aimelodies.models.requests.ArtistUpdateRequest
import com.aimelodies.models.resources.ArtistResource
import com.aimelodies.services.ArtistService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/artists"])
class ArtistController(
    private val artistService: ArtistService,
    private val artistResourceAssembler: ArtistResourceAssembler,
    private val validator: ControllerValidator
) {

    @GetMapping(path = ["/{id}"])
    fun find(
        @PathVariable id: Long
    ): ResponseEntity<ArtistResource> = try {
        ResponseEntity.ok(
            artistResourceAssembler.toModel(
                artistService.find(id)
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    }

    @PutMapping(path = ["/{id}"])
    fun update(
        @PathVariable id: Long,
        @Valid @ModelAttribute artistUpdateRequest: ArtistUpdateRequest,
        bindingResult: BindingResult
    ): ResponseEntity<ArtistResource> = try {
        validator.verifyNonNullErrors(bindingResult)

        ResponseEntity.ok(
            artistResourceAssembler.toModel(
                artistService.update(artistUpdateRequest.copy(id = id))
            )
        )
    } catch (illegalArtistException: IllegalArtistException) {
        throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            illegalArtistException.message,
            illegalArtistException
        )
    }
}