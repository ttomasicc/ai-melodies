package com.aimelodies.controllers.api

import com.aimelodies.controllers.ControllerValidator
import com.aimelodies.controllers.assemblers.AlbumResourceAssembler
import com.aimelodies.exceptions.album.AlbumNotFoundException
import com.aimelodies.exceptions.artist.ArtistNotFoundException
import com.aimelodies.models.requests.AlbumAddRequest
import com.aimelodies.models.requests.AlbumUpdateRequest
import com.aimelodies.models.resources.AlbumResource
import com.aimelodies.services.AlbumService
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
@RequestMapping(path = ["/api/v1/artists/{artistId}/albums"])
class AlbumController(
    private val albumService: AlbumService,
    private val albumResourceAssembler: AlbumResourceAssembler,
    private val validator: ControllerValidator
) {

    @GetMapping(path = [""])
    fun findAll(
        @PathVariable artistId: Long
    ): ResponseEntity<CollectionModel<AlbumResource>> = try {
        ResponseEntity.ok(
            albumResourceAssembler.toCollectionModel(
                albumService.findAll(artistId).toMutableList(),
                artistId
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    }

    @GetMapping(path = ["/{albumId}"])
    fun find(
        @PathVariable artistId: Long,
        @PathVariable albumId: Long
    ): ResponseEntity<AlbumResource> = try {
        ResponseEntity.ok(
            albumResourceAssembler.toModel(
                albumService.find(artistId, albumId)
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    }

    @PostMapping(path = [""])
    fun add(
        @PathVariable artistId: Long,
        @Valid @RequestBody albumAddRequest: AlbumAddRequest?,
        bindingResult: BindingResult
    ): ResponseEntity<AlbumResource> = try {
        validator.verifyNonNullErrors(bindingResult)

        ResponseEntity.ok(
            albumResourceAssembler.toModel(
                albumService.add(
                    albumAddRequest?.copy(artistId = artistId) ?: AlbumAddRequest(artistId = artistId)
                )
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    }

    @PutMapping(path = ["/{albumId}"])
    fun update(
        @PathVariable albumId: Long,
        @Valid @ModelAttribute albumUpdateRequest: AlbumUpdateRequest,
        bindingResult: BindingResult
    ): ResponseEntity<AlbumResource> = try {
        validator.verifyNonNullErrors(bindingResult)

        ResponseEntity.ok(
            albumResourceAssembler.toModel(
                albumService.update(
                    albumUpdateRequest.copy(albumId = albumId)
                )
            )
        )
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    }

    @DeleteMapping(path = ["/{albumId}"])
    fun delete(
        @PathVariable albumId: Long,
    ): ResponseEntity<Unit> = try {
        albumService.delete(albumId)
        ResponseEntity.ok().build()
    } catch (artistNotFoundException: ArtistNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, artistNotFoundException.message, artistNotFoundException)
    } catch (albumNotFoundException: AlbumNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, albumNotFoundException.message, albumNotFoundException)
    }
}