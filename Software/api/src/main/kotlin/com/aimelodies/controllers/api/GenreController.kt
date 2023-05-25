package com.aimelodies.controllers.api

import com.aimelodies.controllers.assemblers.GenreResourceAssembler
import com.aimelodies.controllers.ControllerValidator
import com.aimelodies.exceptions.genre.ExistingGenreException
import com.aimelodies.exceptions.genre.GenreNotFoundException
import com.aimelodies.exceptions.genre.IllegalGenreException
import com.aimelodies.models.requests.GenreRequest
import com.aimelodies.models.resources.GenreResource
import com.aimelodies.services.GenreService
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(path = ["/api/v1/genres"])
class GenreController(
    private val genreService: GenreService,
    private val genreResourceAssembler: GenreResourceAssembler,
    private val validator: ControllerValidator
) {

    @GetMapping(path = [""])
    fun findAll(): ResponseEntity<CollectionModel<GenreResource>> =
        ResponseEntity.ok(
            genreResourceAssembler.toCollectionModel(
                genreService.findAll().toMutableList()
            )
        )

    @PostMapping(path = [""])
    fun add(
        @Valid @RequestBody genreRequest: GenreRequest,
        bindingResult: BindingResult
    ): ResponseEntity<GenreResource> = try {
        validator.verifyErrors(bindingResult)

        ResponseEntity.ok(
            genreResourceAssembler.toModel(
                genreService.add(genreRequest)
            )
        )
    } catch (existingGenreException: ExistingGenreException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, existingGenreException.message, existingGenreException)
    }

    @PutMapping(path = ["/{id}"])
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody genreRequest: GenreRequest,
        bindingResult: BindingResult
    ): ResponseEntity<GenreResource> = try {
        validator.verifyErrors(bindingResult)

        ResponseEntity.ok(
            genreResourceAssembler.toModel(
                genreService.update(genreRequest.copy(id = id))
            )
        )
    } catch (illegalGenreException: IllegalGenreException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, illegalGenreException.message, illegalGenreException)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(
        @PathVariable id: Long
    ): ResponseEntity<Unit> = try {
        if (genreService.delete(id))
            ResponseEntity.ok().build()
        else
            throw ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete genre")
    } catch (genreNotFoundException: GenreNotFoundException) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, genreNotFoundException.message, genreNotFoundException)
    }

    @DeleteMapping(path = [""])
    fun deleteUnused(): ResponseEntity<Unit> {
        genreService.deleteUnused()
        return ResponseEntity.ok().build()
    }
}