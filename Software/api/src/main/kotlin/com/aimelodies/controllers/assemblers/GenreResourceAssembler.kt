package com.aimelodies.controllers.assemblers

import com.aimelodies.controllers.api.GenreController
import com.aimelodies.models.resources.GenreResource
import com.aimelodies.models.views.GenreView
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class GenreResourceAssembler : RepresentationModelAssemblerSupport<GenreView, GenreResource>(
    GenreController::class.java, GenreResource::class.java
) {

    override fun toModel(entity: GenreView): GenreResource =
        createModelWithId(entity.id, entity)

    override fun toCollectionModel(entities: MutableIterable<GenreView>): CollectionModel<GenreResource> =
        CollectionModel.of(
            entities.map { toModel(it) }
        ).add(
            linkTo<GenreController> {
                findAll()
            }.withSelfRel()
        )

    override fun instantiateModel(entity: GenreView): GenreResource =
        GenreResource(
            id = entity.id,
            name = entity.name
        )
}