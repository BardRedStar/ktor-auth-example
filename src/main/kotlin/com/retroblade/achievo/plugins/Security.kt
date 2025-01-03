package com.retroblade.achievo.plugins

import com.retroblade.achievo.models.view.response.ErrorResponse
import com.retroblade.achievo.data.service.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity(tokenService: TokenService) {
    authentication {
        jwt("auth-jwt") {
            realm = tokenService.jwtRealm

            verifier(
                tokenService.jwtVerifier
            )

            validate { credential ->
                tokenService.verifyJWTToken(credential)
            }

            challenge { defaultScheme, realm ->
                val response = ErrorResponse(
                    code = 1,
                    message = "Token is not valid or has expired"
                )
                call.respond(HttpStatusCode.Unauthorized, response)
            }
        }
    }
}
