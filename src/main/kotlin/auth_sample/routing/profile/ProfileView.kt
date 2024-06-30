package auth_sample.routing.profile

import auth_sample.domain.usecases.profile.GetProfileUseCaseImpl
import auth_sample.models.view.response.ErrorResponse
import auth_sample.models.view.response.ProfileResponse
import auth_sample.routing.View
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
                    val userId = call.principal<JWTPrincipal>()?.payload?.subject

                    if (userId == null) {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            ErrorResponse(code = 1, message = "User not allowed")
                        )
                        return@get
                    }

                    val profile = getProfileUseCase(userId)

                    if (profile == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse(code = 1, message = "User not found")
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