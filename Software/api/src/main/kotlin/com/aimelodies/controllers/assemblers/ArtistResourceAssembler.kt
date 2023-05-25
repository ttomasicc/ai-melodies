package com.aimelodies.controllers.assemblers

import com.aimelodies.controllers.api.AlbumController
import com.aimelodies.controllers.api.ArtistController
import com.aimelodies.models.views.ArtistView
import com.aimelodies.models.resources.ArtistResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class ArtistResourceAssembler : RepresentationModelAssemblerSupport<ArtistView, ArtistResource>(
    ArtistController::class.java, ArtistResource::class.java
) {

    override fun toModel(entity: ArtistView): ArtistResource =
        createModelWithId(entity.id, entity).apply {
            add(
                linkTo<AlbumController> {
                    findAll(entity.id)
                }.withRel(Links.ALBUMS.toString())
            )
        }

    override fun instantiateModel(entity: ArtistView): ArtistResource =
        ArtistResource(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            bio = entity.bio,
            image = entity.image,
            dateCreated = entity.dateCreated,
            role = entity.role
        )
}