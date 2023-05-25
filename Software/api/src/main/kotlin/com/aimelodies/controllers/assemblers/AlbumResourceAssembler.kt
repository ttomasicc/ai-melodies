package com.aimelodies.controllers.assemblers

import com.aimelodies.controllers.api.AlbumController
import com.aimelodies.controllers.api.ArtistController
import com.aimelodies.controllers.api.MelodyController
import com.aimelodies.models.resources.AlbumResource
import com.aimelodies.models.views.AlbumView
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class AlbumResourceAssembler : RepresentationModelAssemblerSupport<AlbumView, AlbumResource>(
    AlbumController::class.java, AlbumResource::class.java
) {

    override fun toModel(entity: AlbumView): AlbumResource =
        instantiateModel(entity).apply {
            add(
                linkTo<AlbumController> {
                    find(entity.artist.id, entity.id)
                }.withSelfRel(),
                linkTo<MelodyController> {
                    findAll(entity.artist.id, entity.id)
                }.withRel(Links.MELODIES.toString()),
                linkTo<ArtistController> {
                    find(entity.artist.id)
                }.withRel(Links.ARTIST.toString())
            )
        }

    fun toCollectionModel(entities: MutableIterable<AlbumView>, artistId: Long): CollectionModel<AlbumResource> =
        CollectionModel.of(
            entities.map { toModel(it) }
        ).add(
            linkTo<AlbumController> {
                findAll(artistId)
            }.withSelfRel()
        )

    override fun instantiateModel(entitet: AlbumView): AlbumResource =
        AlbumResource(
            id = entitet.id,
            title = entitet.title,
            description = entitet.description,
            image = entitet.image,
            dateCreated = entitet.dateCreated
        )
}