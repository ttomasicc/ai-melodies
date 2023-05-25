package com.aimelodies.models.resources

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "genres")
data class GenreResource(
    val id: Long,
    val name: String
) : RepresentationModel<GenreResource>()