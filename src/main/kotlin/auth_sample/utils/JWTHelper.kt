package auth_sample.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import java.util.*
import javax.inject.Inject

interface JWTHelper {
    fun createToken(userId: String): String
    fun checkTokenExpiration(token: JWTPrincipal): Boolean
    fun getUserIdFromToken(token: JWTPrincipal): String?
}

class JWTHelperImpl @Inject constructor(): JWTHelper {
    override fun createToken(userId: String): String =
        JWT.create()
            .withAudience(JWT_AUDIENCE)
            .withIssuer(JWT_DOMAIN)
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(JWT_SECRET))

    override fun checkTokenExpiration(token: JWTPrincipal): Boolean {
        val expirationDate = token.expiresAt ?: return false

        return expirationDate.time > System.currentTimeMillis()
    }

    override fun getUserIdFromToken(token: JWTPrincipal): String? {
        return token.subject
    }

    companion object {
        const val JWT_AUDIENCE = "jwt-audience"
        const val JWT_DOMAIN = "https://jwt-provider-domain/"
        const val JWT_REALM = "ktor sample app"
        const val JWT_SECRET = "secret"
    }
}
