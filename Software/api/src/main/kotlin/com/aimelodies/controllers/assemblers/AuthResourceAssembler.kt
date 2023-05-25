package com.aimelodies.controllers.assemblers

import com.aimelodies.controllers.api.AlbumController
import com.aimelodies.controllers.api.AuthController
import com.aimelodies.controllers.api.ArtistController
import com.aimelodies.controllers.assemblers.mocks.MockBindingResult
import com.aimelodies.controllers.assemblers.mocks.MockHttpSession
import com.aimelodies.models.enumerations.ArtistAction
import com.aimelodies.models.views.ArtistAdditionalView
import com.aimelodies.models.resources.ArtistResource
import com.aimelodies.models.requests.ArtistLoginRequest
import com.aimelodies.models.requests.ArtistRegistrationRequest
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class AuthResourceAssembler : RepresentationModelAssemblerSupport<ArtistAdditionalView, ArtistResource>(
    AuthController::class.java, ArtistResource::class.java
) {

    override fun toModel(entity: ArtistAdditionalView): ArtistResource =
        instantiateModel(entity).apply {
            when (entity.action) {
                ArtistAction.REGISTRATION -> {
                    add(
                        linkTo<AuthController> {
                            register(ArtistRegistrationRequest(), MockBindingResult, MockHttpSession)
                        }.withSelfRel(),
                        linkTo<AuthController> {
                            login(ArtistLoginRequest(), MockBindingResult, MockHttpSession)
                        }.withRel(Links.LOGIN.toString())
                    )
                }

                ArtistAction.LOGIN -> {
                    add(
                        linkTo<AuthController> {
                            login(ArtistLoginRequest(), MockBindingResult, MockHttpSession)
                        }.withSelfRel(),
                        linkTo<AuthController> {
                            getToken(MockHttpSession)
                        }.withRel(Links.TOKEN.toString()),
                        linkTo<ArtistController> {
                            find(entity.artist.id)
                        }.withRel(Links.PROFILE.toString()),
                        linkTo<AlbumController> {
                            findAll(entity.artist.id)
                        }.withRel(Links.ALBUMS.toString()),
                        linkTo<AuthController> {
                            logout(MockHttpSession)
                        }.withRel(Links.LOGOUT.toString()),
                    )
                }
            }
        }

    override fun instantiateModel(entity: ArtistAdditionalView): ArtistResource =
        ArtistResource(
            id = entity.artist.id,
            username = entity.artist.username,
            email = entity.artist.email,
            firstName = entity.artist.firstName,
            lastName = entity.artist.lastName,
            bio = entity.artist.bio,
            image = entity.artist.image,
            dateCreated = entity.artist.dateCreated,
            role = entity.artist.role
        )
}