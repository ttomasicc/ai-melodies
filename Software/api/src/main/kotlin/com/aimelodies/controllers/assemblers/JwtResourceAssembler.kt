package com.aimelodies.controllers.assemblers

import com.aimelodies.controllers.api.AuthController
import com.aimelodies.controllers.assemblers.mocks.MockBindingResult
import com.aimelodies.controllers.assemblers.mocks.MockHttpSession
import com.aimelodies.models.views.JwtView
import com.aimelodies.models.resources.JwtResource
import com.aimelodies.models.requests.ArtistLoginRequest
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class JwtResourceAssembler : RepresentationModelAssemblerSupport<JwtView, JwtResource>(
    AuthController::class.java, JwtResource::class.java
) {

    override fun toModel(entity: JwtView): JwtResource =
        instantiateModel(entity).apply {
            add(
                linkTo<AuthController> {
                    getToken(MockHttpSession)
                }.withSelfRel()
            )

            if (entity.token != null) {
                add(
                    linkTo<AuthController> {
                        logout(MockHttpSession)
                    }.withRel(Links.LOGOUT.toString())
                )
            } else {
                add(
                    linkTo<AuthController> {
                        login(ArtistLoginRequest(), MockBindingResult, MockHttpSession)
                    }.withRel(Links.LOGIN.toString())
                )
            }
        }

    override fun instantiateModel(entity: JwtView): JwtResource =
        JwtResource(
            token = entity.token
        )
}