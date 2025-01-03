package com.retroblade.achievo.utils

import com.retroblade.achievo.models.domain.RefreshTokenPayload
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.auth.jwt.*
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

interface TokenUtils {

    val jwtVerifier: JWTVerifier
    val jwtRealm: String

    fun createAccessToken(userId: String): String
    fun verifyJWTToken(credential: JWTCredential): JWTPrincipal?
    fun verifyJWTToken(token: String): DecodedJWT?

    fun createRefreshToken(): String
    fun validateRefreshToken(token: String): Boolean
}

class TokenUtilsImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper
): TokenUtils {

    /// JWT

    override val jwtRealm: String = JWT_REALM

    override val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(JWT_SECRET))
        .withAudience(JWT_AUDIENCE)
        .withIssuer(JWT_DOMAIN)
        .build()

    private val jwtVerifierWithoutExpiration: JWTVerifier = JWT
        .require(Algorithm.HMAC256(JWT_SECRET))
        .withAudience(JWT_AUDIENCE)
        .withIssuer(JWT_DOMAIN)
        .acceptExpiresAt(1_000_000_000)
        .build()

    override fun createAccessToken(userId: String): String =
        JWT.create()
            .withAudience(JWT_AUDIENCE)
            .withIssuer(JWT_DOMAIN)
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_LIFETIME_MILLIS))
            .sign(Algorithm.HMAC256(JWT_SECRET))

    override fun verifyJWTToken(credential: JWTCredential): JWTPrincipal? {
        return if (audienceMatches(credential.payload.audience))
            JWTPrincipal(credential.payload)
        else
            null
    }

    override fun verifyJWTToken(token: String): DecodedJWT? {
        return getDecodedJwtWithoutExpirationCheck(token)?.let {
            if (audienceMatches(it.audience)) it else null
        }
    }

    private fun getDecodedJwtWithoutExpirationCheck(token: String): DecodedJWT? =
        try {
            jwtVerifierWithoutExpiration.verify(token)
        } catch (ex: Exception) {
            null
        }

    private fun audienceMatches(audience: List<String>): Boolean = audience.contains(JWT_AUDIENCE)

    /// Refresh token

    override fun createRefreshToken(): String {
        val expirationDate = System.currentTimeMillis() + REFRESH_TOKEN_LIFETIME_MILLIS

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
        private const val JWT_AUDIENCE = "jwt-audience"
        private const val JWT_DOMAIN = "https://jwt-provider-domain/"
        private const val JWT_REALM = "ktor sample app"
        private const val JWT_SECRET = "secret"
        private const val ACCESS_TOKEN_LIFETIME_MILLIS = 60 * 1000

        private const val REFRESH_TOKEN_SECRET = "irou@!123SADasa"
        private const val REFRESH_TOKEN_LIFETIME_MILLIS = 120 * 1000
    }
}
