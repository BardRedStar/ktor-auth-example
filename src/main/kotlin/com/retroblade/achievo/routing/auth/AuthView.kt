package com.retroblade.achievo.routing.auth

import com.retroblade.achievo.domain.usecases.auth.*
import com.retroblade.achievo.models.domain.RegisterUser
import com.retroblade.achievo.models.view.response.ErrorResponse
import com.retroblade.achievo.models.view.response.TokenResponse
import com.retroblade.achievo.routing.View
import com.retroblade.achievo.routing.auth.validators.LoginValidator
import com.retroblade.achievo.routing.auth.validators.RefreshTokenValidator
import com.retroblade.achievo.routing.auth.validators.RegisterValidator
import com.retroblade.achievo.utils.postValidated
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.inject.Inject

interface AuthView : View

class AuthViewImpl @Inject constructor(
    private val createTokenPairUseCase: CreateTokenPairUseCase,
    private val getUserByCredentialsUseCase: GetUserByCredentialsUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val validateRefreshTokenUseCase: ValidateRefreshTokenUseCase,
    private val verifyAccessTokenUseCase: VerifyAccessTokenUseCase,
) : AuthView {
    override fun setupRouting(application: Application) {
        application.routing {
            postValidated("/login", LoginValidator()) { requestModel ->
                val user = getUserByCredentialsUseCase(requestModel.email, requestModel.password)

                if (user != null) {
                    val token = createTokenPairUseCase(userId = user.id)
                    call.respond(
                        HttpStatusCode.OK,
                        TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken)
                    )
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
                    userName = requestModel.userName,
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

                val token = createTokenPairUseCase(userId = userId)
                call.respond(
                    HttpStatusCode.OK,
                    TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken)
                )
            }

            postValidated("/refreshToken", RefreshTokenValidator()) { requestModel ->
                val userId = verifyAccessTokenUseCase(requestModel.accessToken)

                if (userId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(code = 1, message = "Access token is invalid")
                    )
                    return@postValidated
                }

                val validationResult = validateRefreshTokenUseCase(userId = userId)

                if (!validationResult) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(code = 1, message = "Refresh token was not found, expired or malformed")
                    )
                    return@postValidated
                }

                val token = createTokenPairUseCase(userId = userId)
                call.respond(
                    HttpStatusCode.OK,
                    TokenResponse(accessToken = token.accessToken, refreshToken = token.refreshToken)
                )
            }
        }
    }
}