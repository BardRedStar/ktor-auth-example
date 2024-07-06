package auth_sample.plugins

import auth_sample.models.view.response.ErrorResponse
import auth_sample.data.service.TokenService
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
