package com.retroblade.achievo.repository

import com.retroblade.achievo.models.domain.TokenPair
import com.retroblade.achievo.utils.TokenUtils
import javax.inject.Inject

interface TokenRepository {

    suspend fun saveRefreshTokenForUserId(userId: String, refreshToken: String)

    suspend fun getRefreshTokenForUserId(userId: String): String?

    suspend fun validateRefreshTokenForUserId(userId: String): Boolean

    suspend fun verifyAccessToken(token: String): String?

    suspend fun createTokenPairForUserId(userId: String): TokenPair
}

class TokenRepositoryImpl @Inject constructor(
    private val tokenUtils: TokenUtils,
): TokenRepository {

    private var refreshTokens: MutableMap<String, String> = mutableMapOf()

    override suspend fun saveRefreshTokenForUserId(userId: String, refreshToken: String) {
        refreshTokens[userId] = refreshToken
    }

    override suspend fun getRefreshTokenForUserId(userId: String): String? {
        return refreshTokens[userId]
    }

    override suspend fun validateRefreshTokenForUserId(userId: String): Boolean {
        val token = refreshTokens[userId] ?: return false

        return tokenUtils.validateRefreshToken(token)
    }

    override suspend fun verifyAccessToken(token: String): String? {
        return tokenUtils.verifyJWTToken(token)?.subject
    }

    override suspend fun createTokenPairForUserId(userId: String): TokenPair {
        val accessToken = tokenUtils.createAccessToken(userId)
        val refreshToken = tokenUtils.createRefreshToken()

        refreshTokens[userId] = refreshToken

        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }
}