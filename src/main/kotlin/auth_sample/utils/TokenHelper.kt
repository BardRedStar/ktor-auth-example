package auth_sample.utils

import auth_sample.models.domain.RefreshTokenPayload
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

interface TokenHelper {
    fun createAccessToken(userId: String): String
    fun createRefreshToken(): String
    fun validateRefreshToken(token: String): Boolean
}

class TokenHelperImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper
): TokenHelper {

    override fun createAccessToken(userId: String): String =
        JWT.create()
            .withAudience(JWT_AUDIENCE)
            .withIssuer(JWT_DOMAIN)
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(JWT_SECRET))

    override fun createRefreshToken(): String {
        val expirationDate = System.currentTimeMillis() + 120000

        val checksum = cryptoHelper.hashString(expirationDate.toString() + REFRESH_TOKEN_SECRET)

        val payload = RefreshTokenPayload(expiration = expirationDate, checksum = checksum)

        val refreshToken = cryptoHelper.base64EncodeString(
            Json.encodeToString(serializer = RefreshTokenPayload.serializer(), value = payload)
        )

        return refreshToken
    }

    override fun validateRefreshToken(token: String): Boolean {
        val payloadDecoded = cryptoHelper.base64DecodeString(token)
        val payload = Json.decodeFromString<RefreshTokenPayload>(payloadDecoded)

        val actualChecksum = cryptoHelper.hashString(payload.expiration.toString() + REFRESH_TOKEN_SECRET)

        if (actualChecksum != payload.checksum) {
            return false
        }

        if (payload.expiration <= System.currentTimeMillis()) {
            return false
        }

        return true
    }

    companion object {
        const val JWT_AUDIENCE = "jwt-audience"
        const val JWT_DOMAIN = "https://jwt-provider-domain/"
        const val JWT_REALM = "ktor sample app"
        const val JWT_SECRET = "secret"

        const val REFRESH_TOKEN_SECRET = "irou@!123SADasa"
    }
}
