package com.retroblade.achievo.routing.auth

import com.retroblade.achievo.common.MethodResult
import com.retroblade.achievo.service.AuthService
import com.retroblade.achievo.models.domain.RegisterUser
import com.retroblade.achievo.models.view.request.LoginRequest
import com.retroblade.achievo.models.view.request.RefreshTokenRequest
import com.retroblade.achievo.models.view.request.RegisterRequest
import com.retroblade.achievo.routing.BaseController
import com.retroblade.achievo.routing.auth.validators.LoginValidator
import com.retroblade.achievo.routing.auth.validators.RefreshTokenValidator
import com.retroblade.achievo.routing.auth.validators.RegisterValidator
import com.retroblade.achievo.utils.apiRouting
import com.retroblade.achievo.utils.apiRoutingAuthorized
import com.retroblade.achievo.utils.getAutoResult
import com.retroblade.achievo.utils.postAutoResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import javax.inject.Inject

class AuthController @Inject constructor(
    private val authService: AuthService,
) : BaseController {
    override fun setupRouting(application: Application) {
        application.apiRouting {
            postAutoResult<LoginRequest>("/login", LoginValidator()) { call, requestModel ->
                authService.loginUser(requestModel.email, requestModel.password)
            }

            postAutoResult<RegisterRequest>("/register", RegisterValidator()) { call, requestModel ->
                val registerUserModel = RegisterUser(
                    userName = requestModel.userName,
                    firstName = requestModel.firstName,
                    lastName = requestModel.lastName,
                    email = requestModel.email,
                    password = requestModel.password,
                )
                authService.registerUser(registerUserModel)
            }

            postAutoResult<RefreshTokenRequest>("/refreshToken", RefreshTokenValidator()) { call, requestModel ->
                authService.refreshToken(requestModel.accessToken, requestModel.refreshToken)
            }
        }

        application.apiRoutingAuthorized {
            postAutoResult("/logout") { call ->
                call.request
                    .headers
                    .getTokenOrNull()
                    ?.let { accessToken -> authService.logout(accessToken) }
                    ?: MethodResult.error(HttpStatusCode.BadRequest, "Auth header is not valid")
            }
        }
    }

    private fun Headers.getTokenOrNull(): String? {
        return this.get(AUTH_HEADER_KEY)?.substring(TOKEN_START_INDEX)
    }

    private companion object {
        const val AUTH_HEADER_KEY = "Authorization"
        const val TOKEN_START_INDEX = 7
    }
}