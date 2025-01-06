package com.retroblade.achievo.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.auth.jwt.*
import java.util.*
import javax.inject.Inject

interface TokenUtils {

    val accessTokenJwtVerifier: JWTVerifier
    val jwtRealm: String

    fun createAccessToken(userId: String): String
    fun verifyJWTToken(credential: JWTCredential): JWTPrincipal?
    fun verifyJWTToken(token: String): DecodedJWT?
    fun getUserIdFromToken(token: String): String?

    fun createRefreshToken(userId: String): String
    fun validateRefreshToken(token: String): Boolean
}

class TokenUtilsImpl @Inject constructor(
    private val cryptoHelper: CryptoHelper
) : TokenUtils {

    /// JWT

    override val jwtRealm: String = JWT_REALM

    override val accessTokenJwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(JWT_SECRET))
        .withAudience(JWT_AUDIENCE)
        .withIssuer(JWT_DOMAIN)
        .build()

    private val refreshTokenJwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(REFRESH_TOKEN_SECRET))
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

    override fun getUserIdFromToken(token: String): String? {
        return try {
            JWT.decode(token).subject
        } catch (e: JWTDecodeException) {
            null
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

    override fun createRefreshToken(userId: String): String =
        JWT.create()
            .withAudience(JWT_AUDIENCE)
            .withIssuer(JWT_DOMAIN)
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_LIFETIME_MILLIS))
            .sign(Algorithm.HMAC256(REFRESH_TOKEN_SECRET))

    override fun validateRefreshToken(token: String): Boolean {
        return try {
            refreshTokenJwtVerifier.verify(token)
            true
        } catch (ex: Exception) {
            false
        }
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
