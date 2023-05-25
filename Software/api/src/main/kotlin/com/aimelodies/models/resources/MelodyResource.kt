package com.aimelodies.models.resources

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.Date

@Relation(collectionRelation = "melodies")
data class MelodyResource(
    val id: Long,
    val audio: String,
    val name: String,
    @JsonFormat(timezone="Europe/Zagreb")
    val dateCreated: Date,
    val genre: GenreResource,
) : RepresentationModel<MelodyResource>()