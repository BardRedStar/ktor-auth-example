package com.retroblade.achievo.routing.profile

import com.retroblade.achievo.domain.usecases.profile.GetProfileUseCaseImpl
import com.retroblade.achievo.models.view.response.ErrorResponse
import com.retroblade.achievo.models.view.response.ProfileResponse
import com.retroblade.achievo.routing.View
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.inject.Inject

interface ProfileView: View

class ProfileViewImpl @Inject constructor(
    private val getProfileUseCase: GetProfileUseCaseImpl
): ProfileView {

    override fun setupRouting(application: Application) {
        application.routing {
            authenticate("auth-jwt") {
                get("/profile") {
                    val userId = call.principal<JWTPrincipal>()?.subject

                    if (userId == null) {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            ErrorResponse(
                                code = 1,
                                message = "User not allowed"
                            )
                        )
                        return@get
                    }

                    val profile = getProfileUseCase(userId)

                    if (profile == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse(
                                code = 1,
                                message = "User not found"
                            )
                        )
                        return@get
                    }

                    val response = ProfileResponse(
                        id = profile.id,
                        firstName = profile.firstName,
                        lastName = profile.lastName,
                        email = profile.email
                    )

                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}