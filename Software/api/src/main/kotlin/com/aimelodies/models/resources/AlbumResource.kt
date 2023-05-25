package com.aimelodies.models.resources

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.Date

@Relation(collectionRelation = "albums")
data class AlbumResource(
    val id: Long,
    val title: String,
    val description: String?,
    val image: String?,
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="Europe/Zagreb")
    val dateCreated: Date
) : RepresentationModel<AlbumResource>()