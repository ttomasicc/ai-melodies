package com.aimelodies.models.resources

import org.springframework.hateoas.RepresentationModel

data class JwtResource(
    val token: String?
) : RepresentationModel<JwtResource>()