package com.aimelodies.controllers.api

import com.aimelodies.controllers.assemblers.AlbumResourceAssembler
import com.aimelodies.controllers.assemblers.MelodyResourceAssembler
import com.aimelodies.models.enumerations.ResourceType
import com.aimelodies.models.views.AlbumView
import com.aimelodies.models.views.MelodyAdditionalView
import com.aimelodies.services.AlbumService
import com.aimelodies.services.MelodyService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.math.min

@RestController
@RequestMapping(path = ["/api/v1/info"])
class InfoController(
    private val albumService: AlbumService,
    private val melodyService: MelodyService,

    private val albumResourceAssembler: AlbumResourceAssembler,
    private val melodyResourceAssembler: MelodyResourceAssembler,
) {

    @GetMapping(path = ["/new"])
    fun new(
        @RequestParam(name = "resourceType", defaultValue = ResourceType.Names.ALBUM) resourceType: ResourceType?,
        @RequestParam(name = "page", defaultValue = "0") page: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int
    ): ResponseEntity<PagedModel<out RepresentationModel<*>>> {
        if (resourceType == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid resource type. Available resources: ${enumValues<ResourceType>().joinToString(separator = ", ")}"
            )
        }

        val pageable = PageRequest.of(
            page,
            min(size, 20),
            Sort.by("dateCreated").descending()
        )
        val baseUri = ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("resourceType", resourceType.name)
            .build()

        return ResponseEntity.ok(
            when (resourceType) {
                ResourceType.ALBUM -> PagedResourcesAssembler<AlbumView>(null, baseUri)
                    .toModel(albumService.findAll(pageable), albumResourceAssembler)

                ResourceType.MELODY -> PagedResourcesAssembler<MelodyAdditionalView>(null, baseUri)
                    .toModel(
                        melodyService.findAll(pageable).map {
                            MelodyAdditionalView(it.albums.first().id, it)
                        },
                        melodyResourceAssembler
                    )
            }
        )
    }

    @GetMapping(path = ["/search"])
    fun search(
        @RequestParam(name = "resourceType", defaultValue = ResourceType.Names.ALBUM) resourceType: ResourceType?,
        @RequestParam(name = "title", defaultValue = "") title: String,
        @RequestParam(name = "page", defaultValue = "0") page: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int
    ): ResponseEntity<PagedModel<out RepresentationModel<*>>> {
        if (resourceType == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid resource type. Available resources: ${enumValues<ResourceType>().joinToString(separator = ", ")}"
            )
        }

        val pageable = PageRequest.of(
            page,
            min(size, 20),
            Sort.by("dateCreated").descending()
        )
        val baseUri = ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("resourceType", resourceType.name)
            .replaceQueryParam("title", title)
            .build()

        return ResponseEntity.ok(
            when (resourceType) {
                ResourceType.ALBUM -> PagedResourcesAssembler<AlbumView>(null, baseUri)
                    .toModel(albumService.search(title, pageable), albumResourceAssembler)

                ResourceType.MELODY -> PagedResourcesAssembler<MelodyAdditionalView>(null, baseUri)
                    .toModel(
                        melodyService.search(title, pageable).map {
                            MelodyAdditionalView(it.albums.first().id, it)
                        },
                        melodyResourceAssembler
                    )
            }
        )
    }
}