package com.aimelodies.controllers.api

import com.aimelodies.controllers.assemblers.MelodyResourceAssembler
import com.aimelodies.controllers.ControllerValidator
import com.aimelodies.exceptions.album.AlbumNotFoundException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.exceptions.genre.GenreNotFoundException
import com.aimelodies.exceptions.melody.MelodyNotFoundException
import com.aimelodies.models.requests.MelodyAddRequest
import com.aimelodies.models.requests.MelodyUpdateRequest
import com.aimelodies.models.resources.MelodyResource
import com.aimelodies.models.views.MelodyAdditionalView
import com.aimelodies.services.MelodyService
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/artists/{artistId}/albums/{albumId}/melodies"])
class MelodyController(
    private val melodyService: MelodyService,
    private val melodyResourceAssembler: MelodyResourceAssembler,
    private val validator: ControllerValidator
) {

    @GetMapping(path = [""])
    fun findAll(
        @PathVariable artistId: Long,
        @PathVariable albumId: Long
    ): ResponseEntity<CollectionModel<MelodyResource>> = try {
        ResponseEntity.ok(
            melodyResourceAssembler.toCollectionModel(
                melodyService.findAll(artistId, albumId).toMutableList(),
                artistId,
                albumId
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    } catch (melodyNotFoundException: MelodyNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodyNotFoundException.message, melodyNotFoundException)
    }

    @GetMapping(path = ["/{melodyId}"])
    fun find(
        @PathVariable artistId: Long,
        @PathVariable albumId: Long,
        @PathVariable melodyId: Long,
    ): ResponseEntity<MelodyResource> = try {
        ResponseEntity.ok(
            melodyResourceAssembler.toModel(
                MelodyAdditionalView(albumId, melodyService.find(artistId, albumId, melodyId))
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    } catch (melodyNotFoundException: MelodyNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodyNotFoundException.message, melodyNotFoundException)
    }

    @PostMapping(path = [""])
    fun add(
        @PathVariable artistId: Long,
        @PathVariable albumId: Long,
        @Valid @ModelAttribute melodyAddRequest: MelodyAddRequest?,
        bindingResult: BindingResult
    ): ResponseEntity<MelodyResource> = try {
        if (melodyAddRequest == null)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Missing request body"
            )

        validator.verifyNonNullErrors(
            bindingResult,
            exceptions = listOf(MelodyAddRequest::audio.name)
        )

        val melodyView = melodyService.add(
            melodyAddRequest.copy(
                authorId = artistId,
                albumId = albumId
            )
        )

        ResponseEntity(
            melodyResourceAssembler.toModel(
                MelodyAdditionalView(albumId, melodyView)
            ),
            HttpStatus.CREATED
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    } catch (genreNotFoundException: GenreNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, genreNotFoundException.message, genreNotFoundException)
    }

    @PutMapping(path = ["/{melodyId}"])
    fun update(
        @PathVariable artistId: Long,
        @PathVariable albumId: Long,
        @PathVariable melodyId: Long,
        @Valid @RequestBody melodyUpdateRequest: MelodyUpdateRequest,
        bindingResult: BindingResult
    ): ResponseEntity<MelodyResource> = try {
        validator.verifyErrors(bindingResult)

        val melodyView = melodyService.update(
            melodyUpdateRequest.copy(
                artistId = artistId,
                albumId = albumId,
                melodyId = melodyId
            )
        )

        ResponseEntity.ok(
            melodyResourceAssembler.toModel(
                MelodyAdditionalView(albumId, melodyView)
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    } catch (melodyNotFoundException: MelodyNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodyNotFoundException.message, melodyNotFoundException)
    }

    @DeleteMapping(path = ["/{melodyId}"])
    fun delete(
        @PathVariable artistId: Long,
        @PathVariable albumId: Long,
        @PathVariable melodyId: Long,
    ): ResponseEntity<Unit> = try {
        melodyService.delete(artistId, albumId, melodyId)
        ResponseEntity.ok().build()
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    } catch (melodyNotFoundException: MelodyNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, melodyNotFoundException.message, melodyNotFoundException)
    }
}