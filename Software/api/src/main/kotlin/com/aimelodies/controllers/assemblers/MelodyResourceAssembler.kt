package com.aimelodies.controllers.assemblers

import com.aimelodies.controllers.api.AlbumController
import com.aimelodies.controllers.api.ArtistController
import com.aimelodies.controllers.api.MelodyController
import com.aimelodies.models.views.MelodyAdditionalView
import com.aimelodies.models.views.MelodyView
import com.aimelodies.models.resources.MelodyResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class MelodyResourceAssembler(
    private val genreResourceAssembler: GenreResourceAssembler
) : RepresentationModelAssemblerSupport<MelodyAdditionalView, MelodyResource>(
    MelodyController::class.java, MelodyResource::class.java
) {

    override fun toModel(entity: MelodyAdditionalView): MelodyResource =
        instantiateModel(entity).apply {
            add(
                linkTo<MelodyController> {
                    find(entity.melody.author.id, entity.albumId, entity.melody.id)
                }.withSelfRel(),
                linkTo<AlbumController> {
                    find(entity.melody.author.id, entity.albumId)
                }.withRel(Links.ALBUM.toString()),
                linkTo<ArtistController> {
                    find(entity.melody.author.id)
                }.withRel(Links.ARTIST.toString())
            )
        }

    fun toCollectionModel(
        entities: MutableIterable<MelodyView>,
        artistId: Long,
        albumId: Long
    ): CollectionModel<MelodyResource> =
        CollectionModel.of(
            entities.map { toModel(MelodyAdditionalView(albumId, it)) }
        ).add(
            linkTo<MelodyController> {
                findAll(artistId, albumId)
            }.withSelfRel()
        )

    override fun instantiateModel(entity: MelodyAdditionalView): MelodyResource =
        MelodyResource(
            id = entity.melody.id,
            audio = entity.melody.audio,
            name = entity.melody.name,
            dateCreated = entity.melody.dateCreated,
            genre = genreResourceAssembler.toModel(entity.melody.genre)
        )
}