package auth_sample.plugins

import auth_sample.models.view.response.ErrorResponse
import auth_sample.utils.TokenHelperImpl
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    authentication {
        jwt("auth-jwt") {
            realm = TokenHelperImpl.JWT_REALM
            verifier(
                JWT
                    .require(Algorithm.HMAC256(TokenHelperImpl.JWT_SECRET))
                    .withAudience(TokenHelperImpl.JWT_AUDIENCE)
                    .withIssuer(TokenHelperImpl.JWT_DOMAIN)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(TokenHelperImpl.JWT_AUDIENCE))
                    JWTPrincipal(credential.payload)
                else
                    null
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
