package auth_sample.routing.auth

import auth_sample.domain.usecases.auth.*
import auth_sample.models.domain.RegisterUser
import auth_sample.models.view.response.ErrorResponse
import auth_sample.models.view.response.TokenResponse
import auth_sample.routing.View
import auth_sample.routing.auth.validators.LoginValidator
import auth_sample.routing.auth.validators.RegisterValidator
import auth_sample.utils.JWTHelper
import auth_sample.utils.postValidated
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.inject.Inject

interface AuthView: View

class AuthViewImpl @Inject constructor(
    private val jwtHelper: JWTHelper,
    private val getUserByCredentialsUseCase: GetUserByCredentialsUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val registerUserUseCase: RegisterUserUseCase
): AuthView {
    override fun setupRouting(application: Application) {
        application.routing {
            postValidated("/login", LoginValidator()) { requestModel ->
                val user = getUserByCredentialsUseCase(requestModel.email, requestModel.password)

                if (user != null) {
                    val token = jwtHelper.createToken(user.id)
                    call.respond(HttpStatusCode.OK, TokenResponse(token = token))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(
                            code = 1,
                            message = "User not found for such credentials"
                        )
                    )
                }
            }

            postValidated("/register", RegisterValidator()) { requestModel ->
                val existingUser = getUserByEmailUseCase(requestModel.email)
                if (existingUser != null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            code = 1,
                            message = "User already exists for this email"
                        )
                    )
                    return@postValidated
                }

                val registerUser = RegisterUser(
                    firstName = requestModel.firstName,
                    lastName = requestModel.lastName,
                    email = requestModel.email,
                    password = requestModel.password,
                )

                val userId = registerUserUseCase(registerUser)

                if (userId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(code = 1, message = "Something went wrong")
                    )
                    return@postValidated
                }

                val token = jwtHelper.createToken(userId)
                call.respond(HttpStatusCode.OK, TokenResponse(token = token))
            }
        }
    }
}